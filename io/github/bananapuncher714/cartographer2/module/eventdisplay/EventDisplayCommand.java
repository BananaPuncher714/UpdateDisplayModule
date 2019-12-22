package io.github.bananapuncher714.cartographer2.module.eventdisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class EventDisplayCommand implements CommandExecutor, TabCompleter {
	private EventDisplayModule module;
	
	protected EventDisplayCommand( EventDisplayModule module ) {
		this.module = module;
	}
	
	@Override
	public boolean onCommand( CommandSender sender, Command arg1, String arg2, String[] args ) {
		if ( sender.hasPermission( module.getPermission() ) ) {
			if ( args.length == 1 ) {
				if ( args[ 0 ].equalsIgnoreCase( "setcolor" ) ) {
					sender.sendMessage( ChatColor.WHITE + "The current color is " + ChatColor.YELLOW + module.getColor() );
				} else {
					sender.sendMessage( ChatColor.RED + "Usage: /eventdisplay <setcolor|setenabled> ..." );
				}
			} else if ( args.length == 2 ) {
				if ( args[ 0 ].equalsIgnoreCase( "setcolor" ) ) {
					int color = 0;
					try {
						color = Integer.valueOf( args[ 1 ] );
					} catch ( Exception exception ) {
						sender.sendMessage( ChatColor.RED + "You must enter an integer!" );
						return false;
					}
					
					module.setColor( color );
					sender.sendMessage( ChatColor.WHITE + "Successfully set the color to " + ChatColor.YELLOW + color );
				} else if ( args[ 0 ].equalsIgnoreCase( "setenabled" ) ) {
					boolean value = true;
					try {
						value = Boolean.valueOf( args[ 1 ] );
					} catch ( Exception exception ) {
						sender.sendMessage( ChatColor.RED + "You must enter true or false!" );
						return false;
					}
					
					module.setOn( value );
					if ( value ) {
						sender.sendMessage( ChatColor.WHITE + "Turned EventDisplay " + ChatColor.YELLOW + "on" );
					} else {
						sender.sendMessage( ChatColor.WHITE + "Turned EventDisplay " + ChatColor.YELLOW + "off" );
					}
				} else {
					sender.sendMessage( ChatColor.RED + "Usage: /eventdisplay <setcolor|setenabled> ..." );
				}
			} else {
				sender.sendMessage( ChatColor.RED + "Usage: /eventdisplay <setcolor|setenabled> ..." );
			}
		} else {
			sender.sendMessage( ChatColor.RED + "You do not have permission to run this command!" );
		}
		return false;
	}

	@Override
	public List< String > onTabComplete( CommandSender sender, Command arg1, String arg2, String[] args ) {
		List< String > aos = new ArrayList< String >();
		if ( !sender.hasPermission( module.getPermission() ) ) {
			return aos;
		}

		if ( args.length == 1 ) {
			aos.add( "setcolor" );
			aos.add( "setenabled" );
		} else if ( args.length == 2 ) {
			if ( args[ 0 ].equalsIgnoreCase( "setenabled" ) ) {
				aos.add( "true" );
				aos.add( "false" );
			}
		}
			
		List< String > completions = new ArrayList< String >();
		StringUtil.copyPartialMatches( args[ args.length - 1 ], aos, completions );
		Collections.sort( completions );
		return completions;
	}

}
