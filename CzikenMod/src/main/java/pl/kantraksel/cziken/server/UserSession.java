package pl.kantraksel.cziken.server;

import net.minecraft.util.math.Vec3d;

public class UserSession {
	public int ticksWithoutAuth = 0;
	public Vec3d initPosition = Vec3d.ZERO;
	
	public UserSession(Vec3d initialPosition) {
		initPosition = initialPosition;
	}
}
