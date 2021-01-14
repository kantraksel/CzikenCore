package pl.kantraksel.cziken;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import pl.kantraksel.cziken.authentication.AuthenticationSystem;
import pl.kantraksel.cziken.commands.*;
import pl.kantraksel.cziken.network.RequestAuthMessageHandler;
import pl.kantraksel.cziken.network.ResponseAuthMessageHandler;
import pl.kantraksel.cziken.network.messages.RequestAuthMessage;
import pl.kantraksel.cziken.network.messages.ResponseAuthMessage;

import org.apache.logging.log4j.Logger;

@Mod(modid = CzikenCore.MODID, name = CzikenCore.NAME, version = CzikenCore.VERSION)
public class CzikenCore {
    public static final String MODID = "czikencore";
    public static final String NAME = "CzikenCore 4";
    public static final String VERSION = "1.0";

    @Instance(MODID) 
    public static CzikenCore INSTANCE;
    public static Logger logger;
    
    public final SimpleNetworkWrapper NetworkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public final AuthenticationSystem AuthSystem = new AuthenticationSystem();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        AuthSystem.initialize(event.getModConfigurationDirectory().getPath());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	NetworkChannel.registerMessage(RequestAuthMessageHandler.class, RequestAuthMessage.class, 1, Side.CLIENT);
    	NetworkChannel.registerMessage(ResponseAuthMessageHandler.class, ResponseAuthMessage.class, 0, Side.SERVER);
    	
    	if (getPhysicalSide() == Side.CLIENT) {
    		if (AuthSystem.initalizeClient()) logger.info("Client initialized successfully");
    		else logger.error("Failed to initialize client");
    	}
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	if (getPhysicalSide() == Side.SERVER) 
    		if (AuthSystem.initializeServer()) logger.info("Server initialized successfully");
    		else logger.error("Failed to initialize server");
    	
    	event.registerServerCommand(new ReloadAuthCommand());
    	event.registerServerCommand(new AddPlayerCommand());
    	event.registerServerCommand(new RemovePlayerCommand());
    }
    
    @EventHandler
    public void serverUnload(FMLServerStoppingEvent event) {
    	AuthSystem.shutdown();
    }
    
    //Forge shortcuts
    public static MinecraftServer getServerInstance() {
    	return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    public static Side getLogicalSide() { //unused
    	return FMLCommonHandler.instance().getEffectiveSide();
    }
    
    public static Side getPhysicalSide() {
    	return FMLCommonHandler.instance().getSide();
    }
}
