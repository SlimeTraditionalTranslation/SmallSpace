package me.CAPS123987.smallspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.updater.GitHubBuildsUpdaterTR;
import me.CAPS123987.cargo.SpaceInterface;
import me.CAPS123987.cargo.ExportBus;
import me.CAPS123987.cargo.ImportBus;
import me.CAPS123987.dimension.SmallSpaceDim;
import me.CAPS123987.implementation.SizedBlock;
import me.CAPS123987.implementation.Teleporterss;
import me.CAPS123987.items.Items;
import me.CAPS123987.machines.BlockAssigner;
import me.CAPS123987.tabCompleater.*;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.mini2Dx.gettext.GetText;
import org.mini2Dx.gettext.PoFile;


public class SmallSpace extends JavaPlugin implements SlimefunAddon, Listener {
	public static SmallSpace instance;
	Config cfg = new Config(this);
    @Override
    public void onEnable() {
    	
    	this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    	
        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("EFI - ")) {
        	
        	GitHubBuildsUpdaterTR
        	updater 
        	= 
        	new GitHubBuildsUpdaterTR(
        			this
        			, this.getFile()
        			, "SlimeTraditionalTranslation/SmallSpace/master");
        	
        	updater.start();
        	
        }
    	
    	instance = this;

		GetText.setLocale(Locale.TRADITIONAL_CHINESE);
		InputStream inputStream = getClass().getResourceAsStream("/translations/zh_tw.po");
		if (inputStream == null) {
			getLogger().severe("錯誤！無法找到翻譯檔案，請回報給翻譯者。");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {
			getLogger().info("載入繁體翻譯檔案...");
			try {
				PoFile poFile = new PoFile(Locale.TRADITIONAL_CHINESE, inputStream);
				GetText.add(poFile);
			} catch (ParseCancellationException | IOException e) {
				getLogger().severe("錯誤！讀取翻譯時發生錯誤，請回報給翻譯者：" + e.getMessage());
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}

		getCommand("SmallSpace").setTabCompleter(new TabC());
        //setup dimension 
        WorldCreator worldCreator = new WorldCreator("SmallSpace");
        worldCreator.generator(new SmallSpaceDim());
        worldCreator.createWorld();
        
        new SlimefunItem(Items.smallSpace, Items.SPACE_STABILIZER, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_SPACE_STABILIZER).register(this);
        //this.getServer().getPluginManager().registerEvents(CommandListener.onCommand(null, null, getBugTrackerURL(), null),this);
        new SizedBlock(1,Items.SIZED_BLOCK1,Items.recipe_SIZED_BLOCK1).register(this);
        new SizedBlock(2,Items.SIZED_BLOCK2,Items.recipe_SIZED_BLOCK2).register(this);
        new SizedBlock(3,Items.SIZED_BLOCK3,Items.recipe_SIZED_BLOCK3).register(this);
        new SizedBlock(4,Items.SIZED_BLOCK4,Items.recipe_SIZED_BLOCK4).register(this);
        new Teleporterss().register(this);
        new BlockAssigner().register(this);
        
        new SpaceInterface().register(this);
        ImportBus ibus = new ImportBus();
        ibus.register(this);
        ibus.setHidden(true);

        ExportBus ebus = new ExportBus();
        ebus.register(this);
        ebus.setHidden(true);
        
        
    }
    

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new SmallSpaceDim();
    }
    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * You will need to return a reference to your Plugin here.
         * If you are using your main class for this, simply return "this".
         */
        return this;
    }
    public static SmallSpace getInstance() {
        return instance;
    }
    
    @EventHandler
    public void blockPlace(BlockBreakEvent e) {
    	Material m = e.getBlock().getType();
    	String name = e.getBlock().getWorld().getName();
    	if(m==Material.BEDROCK&&name.equals("SmallSpace")) {
    		e.setCancelled(true);
    	}
    	
    }
    
    /*
     * set bukkit.yml
     */
    @Override
    public boolean onCommand(CommandSender p, Command command, String label, String[] args) {
    	
    	
    	
    	
    	
    	
    	switch(args[0]) {
    		case "help":
    			help(p);
    			break;
	    	case "tptoId":
	    		if (!p.hasPermission("SmallSpace.admin")) {
	        		p.sendMessage(ChatColor.RED+GetText.tr("No permition"));
	        		return false;
	        	}
	    		if(p instanceof Player) {
		    		try {
		    			String id = args[1];
		    			int value = Integer.parseInt(id.replaceAll("[^0-9]", ""));
		    			}catch(Exception e) {
		    			p.sendMessage(GetText.tr("Please enter Id of space"));
		    			return true;
		    		}
		    		String id = args[1];
		    		int value = Integer.parseInt(id.replaceAll("[^0-9]", ""));
		    		tryToTeleport(Calculator.getLoc(id),p);
	    		}else {
	    			notSeder(p);
	    			return true;
	    		}
	    		
	    		break;
	    		
	    		
	    	case "blockId":
	    		if (!p.hasPermission("SmallSpace.admin")) {
	        		p.sendMessage(ChatColor.RED+GetText.tr("No permition"));
	        		return false;
	        	}
	    		try {
	    			String id1 = args[1];
	    			int value1 = Integer.parseInt(id1.replaceAll("[^0-9]", ""));
	    			}catch(Exception e) {
	    			p.sendMessage(GetText.tr("Please enter Id of space"));
	    			return true;
	    		}
	    		String id1 = args[1];
	    		int value1 = Integer.parseInt(id1.replaceAll("[^0-9]", ""));
	    		Calculator.getLoc(id1).getBlock().setType(Material.BARRIER);
	    		
	    		break;
	    		
	    		
	    	case "unblockId":
	    		if (!p.hasPermission("SmallSpace.admin")) {
	        		p.sendMessage(ChatColor.RED+GetText.tr("No permition"));
	        		return false;
	        	}
	    		try {
	    			String id2 = args[1];
	    			int value2 = Integer.parseInt(id2.replaceAll("[^0-9]", ""));
	    			}catch(Exception e) {
	    			p.sendMessage(GetText.tr("Please enter Id of space"));
	    			return true;
	    		}
	    		String id2 = args[1];
	    		int value2 = Integer.parseInt(id2.replaceAll("[^0-9]", ""));
	    		Calculator.getLoc(id2).getBlock().setType(Material.AIR);
	    		
	    		break;
	    		
	    		
	    	case "teleportRemove":
	    		if (!p.hasPermission("SmallSpace.admin")) {
	        		p.sendMessage(ChatColor.RED+GetText.tr("No permition"));
	        		return false;
	        	}
	    		if(!(p instanceof Player)) {
	    			notSeder(p);
	    			return true;
	    		}
	    		
	    		Player pp;
	    		pp = (Player) p;
	    		Block b =pp.getTargetBlockExact(20);
	    		if(!BlockStorage.hasBlockInfo(b)) {
	    			p.sendMessage(GetText.tr("this block doesn't have BlockStorage data"));
	    			return true;
	    		}
	    		if(!BlockStorage.getLocationInfo(b.getLocation(),"id").equals("TELEPORT")) {
	    			p.sendMessage(GetText.tr("this block is not teleporter"));
	    			return true;
	    		}
	    		BlockStorage.clearBlockInfo(b);
	    		
	    		break;
	    		
		    	case "memberAdd":
		    		if(1<args.length) {
		    			if(p instanceof Player) {
		    				groupAdd(p,args);
		    			}else {
		    				notSeder(p);
		    			}
		    		}
		    		return true;
		    		
		    		
		    	case "memberRemove":
		    		if(1<args.length) {
		    			if(p instanceof Player) {
		    				groupRemove(p,args);
		    			}else {
		    				notSeder(p);
		    			}
		    		}
		    		return true;
	    	
	    	default:
	    		p.sendMessage(GetText.tr("Unknown command"));
    	}
    	
    	return true;
		
	}
    public void tryToTeleport(Location l,CommandSender p) {
    	World world = Bukkit.getWorld("SmallSpace");
    	Location newloc2 = new Location(world,l.getX()+0.5,l.getY()+1,l.getZ()+0.5);
    	if(newloc2.getBlock().getType()==Material.BEDROCK) {
    		p.sendMessage(GetText.tr("Space is not initialized yet"));
    		return;
    	}
    	Player p2 = (Player) p;
    	p2.teleport(newloc2);
    	
    }
    public void notSeder(CommandSender p) {
    	p.sendMessage(GetText.tr("You must by Player to use this command"));
    }
    public void help(CommandSender p) {
    	p.sendMessage(ChatColor.GOLD+GetText.tr("memberAdd: ")+ChatColor.WHITE+GetText.tr("This command adds Players to your Space(to use you need to look on Sized Block)"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("memberRemove: ")+ChatColor.WHITE+GetText.tr("This command removes Players to your Space(to use you need to look on Sized Block)"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("help: ")+ChatColor.WHITE+GetText.tr("This command displaies help list"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("tptoId: ")+ChatColor.WHITE+GetText.tr("This command teleports you to Space with given id"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("blockId: ")+ChatColor.WHITE+GetText.tr("This command blocks Space with given id"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("unblockId: ")+ChatColor.WHITE+GetText.tr("This command unblocks Space with given id"));
    	p.sendMessage(ChatColor.GOLD+GetText.tr("teleportRemove: ")+ChatColor.WHITE+GetText.tr("This command removes teleporter block(to use you need to look on TELEPORTER block)"));
    	
    }
    
    
    public boolean groupAdd(CommandSender p, String[] args) {
    	
    	final Player pp = (Player) p;
		Block b =pp.getTargetBlockExact(20);
		if(!BlockStorage.hasBlockInfo(b)) {
			p.sendMessage(GetText.tr("this block doesn't have BlockStorage data"));
			return false;
		}

		SlimefunItem item = BlockStorage.check(b);
		if (!(item instanceof SizedBlock)) {
			p.sendMessage(GetText.tr("this block is not SIZED BLOCK"));
			return false;
		}
		
		if(!(BlockStorage.getLocationInfo(b.getLocation(),"owner").equals(pp.getName())) && !p.hasPermission("SmallSpace.admin")) {
			pp.sendMessage(GetText.tr("You are not owner of this block"));
			return false;
		}
		
		List<String> info = Calculator.playersGet(BlockStorage.getLocationInfo(b.getLocation(), "Players"));
		if(info.contains(args[1])) {
			return false;
		}
		
		String old = BlockStorage.getLocationInfo(b.getLocation(), "Players");
		
		if(old == null) {
			BlockStorage.addBlockInfo(b, "Players",args[1]+";");
		}else {
			BlockStorage.addBlockInfo(b, "Players",old + args[1] + ";");
    	}
		pp.sendMessage(GetText.tr("Player {0} added", args[1]));
		return true;
		
		
    }
    
    
    
    public boolean groupRemove(CommandSender p, String[] args) {
    	
    	final Player pp = (Player) p;
		Block b =pp.getTargetBlockExact(20);
		if(!BlockStorage.hasBlockInfo(b)) {
			p.sendMessage(GetText.tr("this block doesn't have BlockStorage data"));
			return false;
		}
		
		SlimefunItem item = BlockStorage.check(b);
		if (!(item instanceof SizedBlock)) {
			p.sendMessage(GetText.tr("this block is not SIZED BLOCK"));
			return false;
		}
		
		if(!(BlockStorage.getLocationInfo(b.getLocation(),"owner").equals(pp.getName())) && !p.hasPermission("SmallSpace.admin")) {
			pp.sendMessage(GetText.tr("You are not owner of this block"));
			return false;
		}
		
		String arg = args[1];
		
		List<String> info = Calculator.playersGet(BlockStorage.getLocationInfo(b.getLocation(), "Players"));
		
		if(info.contains(args[1])) {
				
			if(info.contains(arg)) {
				info.remove(info.indexOf(arg));
			}	
			String text = "";
			for(String s : info) {
				text = text + s + ";";
				
			}
			BlockStorage.addBlockInfo(b, "Players", text);
			pp.sendMessage(GetText.tr("Player {0} removed", args[1]));
			
			return true;
		}else {
			pp.sendMessage(GetText.tr("Player {0} isn't registered in this block", args[1]));
			return false;
		}
		
		
		
    }

}
