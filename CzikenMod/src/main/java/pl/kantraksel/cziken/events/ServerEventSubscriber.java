package pl.kantraksel.cziken.events;

import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pl.kantraksel.cziken.CzikenCore;

@EventBusSubscriber(modid = CzikenCore.MODID)
public class ServerEventSubscriber {
	@SubscribeEvent
	public static void onClientConnect(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP)
			CzikenCore.INSTANCE.AuthSystem.Server.onPlayerConnected((EntityPlayerMP)event.player);
	}
		
	@SubscribeEvent
	public static void onClientDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.player instanceof EntityPlayerMP) 
			CzikenCore.INSTANCE.AuthSystem.Server.onPlayerDisconnected((EntityPlayerMP)event.player);
	}
		
	@SubscribeEvent
	public static void onTick(TickEvent.ServerTickEvent event) {
		CzikenCore.INSTANCE.AuthSystem.Server.onTick();
	}
	
	@SubscribeEvent
	public static void onBanCommand(CommandEvent event) {
		if (event.getCommand() instanceof CommandBanPlayer) {
			if (event.getParameters().length > 0) 
				CzikenCore.INSTANCE.AuthSystem.Server.sheuldeCheckBans();
		}
	}
	
	//Protection Event Short Functions
	static boolean handleChatEvent(EntityPlayerMP player) {
		if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName())) {
			player.sendMessage(new TextComponentTranslation("text.czikencore.authrequired"));
			return true; //Cancel Event
		}
		return false;
	}
	
	//Protection Events
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onServerChat(ServerChatEvent event) {
		event.setCanceled(handleChatEvent(event.getPlayer()));
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onCommand(CommandEvent event) {
		if (event.getSender() instanceof EntityPlayerMP)
			event.setCanceled(handleChatEvent((EntityPlayerMP)event.getSender()));
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onAttackEntity(AttackEntityEvent event) {
		if (event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName()))
				event.setCanceled(true);
		}
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPickupItem(EntityItemPickupEvent event) {
		if (event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName()))
				event.setCanceled(true);
		}
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onDropItem(ItemTossEvent event) {
		if (event.getPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getPlayer();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName())) {
				event.setCanceled(true);
				player.inventory.addItemStackToInventory(event.getEntityItem().getItem());
				player.inventoryContainer.detectAndSendChanges();
			}
		}
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onInteractEvent(PlayerInteractEvent event) {
		if (event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName())) {
				event.setCanceled(true);
				player.inventoryContainer.detectAndSendChanges(); //placing blocks enforces it
			}
		}
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTargetPlayerByMob(LivingSetAttackTargetEvent event) {
		if (event.getTarget() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getTarget();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName()))
				((EntityLiving)event.getEntityLiving()).setAttackTarget(null);
		}
	}
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onDamagePlayer(LivingAttackEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
			if (!CzikenCore.INSTANCE.AuthSystem.Server.isPlayerAuthenticated(player.getName()))
				event.setCanceled(true);
		}
	}
}
