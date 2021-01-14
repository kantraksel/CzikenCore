package pl.kantraksel.cziken.authentication;

public interface IModule {
	public boolean initialize(AuthenticationSystem parent);
	public void shutdown();
	public boolean isActive();
}
