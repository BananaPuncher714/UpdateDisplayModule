package io.github.bananapuncher714.cartographer2.module.eventdisplay;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import io.github.bananapuncher714.cartographer.core.api.events.MinimapLoadEvent;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import io.github.bananapuncher714.cartographer.core.module.Module;
import io.github.bananapuncher714.cartographer.core.util.FileUtil;

public class EventDisplayModule extends Module implements Listener {
	private Set< Location > locations = new HashSet< Location >();
	private boolean on = true;
	private int color = 0xFFFF0000;
	private String permission = "cartographer2.module.eventdisplay.admin";
	private File config;
	
	@Override
	public void onEnable() {
		// Save our config
		config = new File( getDataFolder() + File.separator + "config.yml" );
		FileUtil.saveToFile( getResource( "config.yml" ), config, false );
		
		// Load our config
		loadConfig();
		
		// Register this to listen for relevant events
		registerListener( this );
		
		// Run a runnable every 5 ticks
		Bukkit.getScheduler().runTaskTimer( getCartographer(), this::update, 0, 5 );
		
		// Register a new command and set the executor and other related stuff
		EventDisplayCommand executor = new EventDisplayCommand( this );
		PluginCommand command = getCommand( "eventdisplay" );
		command.setExecutor( executor );
		command.setTabCompleter( executor );
		command.setPermission( permission );
		command.setDescription( "Command for customizing EventDisplay" );
	}
	
	private void loadConfig() {
		FileConfiguration configuration = YamlConfiguration.loadConfiguration( config );
		permission = configuration.getString( "permission", "cartographer2.module.eventdisplay.admin" );
	}
	
	private void update() {
		// Clear the unnecessary locations
		locations.clear();
	}
	
	protected Set< Location > getLocations() {
		return locations;
	}
	
	public void setOn( boolean on ) {
		this.on = on;
	}
	
	public boolean isOn() {
		return on;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor( int color ) {
		this.color = color;
	}
	
	public String getPermission() {
		return permission;
	}

	@EventHandler
	private void onEvent( MinimapLoadEvent event ) {
		// We want to insert our PixelProvider into every new map
		// All maps get loaded after modules, so we will see this event called for every map
		Minimap map = event.getMinimap();
		map.registerWorldPixelProvider( new EventDisplayWorldPixelProvider( this ) );
	}

	@EventHandler
	private void onEvent( BlockPhysicsEvent event ) {
		// We're tracking which blocks get affected by this event, so listen for this too
		Location old = event.getBlock().getLocation();
		locations.add( new Location( old.getWorld(), old.getBlockX(), 0, old.getBlockZ() ) );
	}
}
