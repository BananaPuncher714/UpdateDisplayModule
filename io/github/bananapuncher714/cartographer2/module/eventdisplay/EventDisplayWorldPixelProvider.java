package io.github.bananapuncher714.cartographer2.module.eventdisplay;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.cartographer.core.api.WorldPixel;
import io.github.bananapuncher714.cartographer.core.api.map.WorldPixelProvider;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import io.github.bananapuncher714.cartographer.core.renderer.CartographerRenderer.PlayerSetting;

public class EventDisplayWorldPixelProvider implements WorldPixelProvider {
	private EventDisplayModule module;
	
	public EventDisplayWorldPixelProvider( EventDisplayModule module ) {
		this.module = module;
	}
	
	@Override
	public Collection< WorldPixel > getWorldPixels( Player player, Minimap map, PlayerSetting setting ) {
		Set< WorldPixel > pixels = new HashSet< WorldPixel >();
		
		if ( module.isOn() && player.hasPermission( module.getPermission() ) ) {
			// Create a new WorldPixel from every location, with the given color
			for ( Location location : module.getLocations() ) {
				WorldPixel pixel = new WorldPixel( location.getWorld(), location.getBlockX(), location.getBlockZ(), new Color( module.getColor() ) );
				pixel.setDepth( 65535 );
				pixels.add( pixel );
			}
			
			Location loc = setting.getLocation();
			WorldPixel chunkPix = new WorldPixel( loc.getWorld(), loc.getBlockX() & ~0xF, loc.getBlockZ() & ~0xF , new Color( 255, 255, 0, 128 ) );
			chunkPix.setWidth( 16 );
			chunkPix.setHeight( 16 );
			chunkPix.setDepth( 0 );
			
			pixels.add( chunkPix );
		}
		
		return pixels;
	}

}
