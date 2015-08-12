package tk.itstake.steakgui.editor.taskeditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.editor.ItemTaskEditor;
import tk.itstake.steakgui.editor.NewTaskSelector;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.ItemStackConverter;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class GiveTaskEditor {
    public void show(Menu menu, Player player, String menuname, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "..";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4����:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, menuname, player, task, 0, 1,  slot, SteakGUI.convertMessage("&c�޹̼� �ޱ�"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b�޴� ��ǰ�� �޹̼����� �����մϴ�.")}));
        setting.setItem(1, new ItemTaskItem(menu, menuname, player, task, 1, 1,  slot, SteakGUI.convertMessage("&c������ �ޱ�"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b�޴� ��ǰ�� ���������� �����մϴ�.")}));
        setting.setItem(2, new ItemTaskItem(menu, menuname, player, task, 2, 1,  slot, SteakGUI.convertMessage("&c����ġ �ޱ�"), Material.ENCHANTMENT_TABLE, new String[]{SteakGUI.convertMessage("&b�޴� ��ǰ�� ����ġ�� �����մϴ�.")}));
        setting.setItem(3, new ItemTaskItem(menu, menuname, player, task, 3, 1,  slot, SteakGUI.convertMessage("&c���� �ޱ�"), Material.EXP_BOTTLE, new String[]{SteakGUI.convertMessage("&b�޴� ��ǰ�� �������� �����մϴ�.")}));
        if(((String)edittask.getData()[0]).equals("permission")) {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 4, 1,  slot, SteakGUI.convertMessage("&b�޹̼� �Է�"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b�����ϰ� �� �޹̼��� �Է��մϴ�.")}));
        } else if(((String)edittask.getData()[0]).equals("item")) {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 5, 1,  slot, SteakGUI.convertMessage("&b������ ����"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b�����ϰ� �� �������� �տ��� �����մϴ�.")}));
        } else if(((String)edittask.getData()[0]).equals("exp")) {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 6, 1,  slot, SteakGUI.convertMessage("&b������ ����"), Material.ENCHANTMENT_TABLE, new String[]{SteakGUI.convertMessage("&b�����ϰ� �� �������� �տ��� �����մϴ�.")}));
        } else if(((String)edittask.getData()[0]).equals("level")) {
            setting.setItem(9, new ItemTaskItem(menu, menuname, player, task, 7, 1,  slot, SteakGUI.convertMessage("&b������ ����"), Material.EXP_BOTTLE, new String[]{SteakGUI.convertMessage("&b�����ϰ� �� �������� �տ��� �����մϴ�.")}));
        }
        setting.setItem(18, new ItemTaskItem(menu, menuname, player, task, 8, 1,  slot, SteakGUI.convertMessage("&b�۾� ���� ����"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b�۾� ������ ���� �մϴ�.")}));
        setting.setItem(19, new ItemTaskItem(menu, menuname, player, task, 9, 1,  slot, SteakGUI.convertMessage("&b�۾� ����"), Material.BARRIER, new String[]{SteakGUI.convertMessage("&b�۾��� �����մϴ�.")}));
        setting.setItem(20, new ItemTaskItem(menu, menuname, player, task, 99, 1, slot, SteakGUI.convertMessage("&c���ư���"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c���� �Ŵ��� ���ư��ϴ�.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int t = 0;
        int task = 0;
        int slot = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        public ItemTaskItem(Menu lmenu, String menuName, Player p, int ltask, int type, int amount, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, amount), lore);
            slot = s;
            t = type;
            task = ltask;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
            if(t == 0) {
                editTask.getData()[0] = "permission";
                new MessageHandler().sendMessage(event.getPlayer(), "&c���� �޹̼��� �ް� �˴ϴ�.");
            } else if(t == 1) {
                editTask.getData()[0] = "item";
                new MessageHandler().sendMessage(event.getPlayer(), "&c���� �������� �ް� �˴ϴ�.");
            } else if(t == 2) {
                editTask.getData()[0] = "exp";
                new MessageHandler().sendMessage(event.getPlayer(), "&c���� ����ġ�� �ް� �˴ϴ�.");
            } else if(t == 3) {
                editTask.getData()[0] = "level";
                new MessageHandler().sendMessage(event.getPlayer(), "&c���� ������ �ް� �˴ϴ�.");
            } else if(t == 4) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a�ް� �� �޹̼��� �Է��ϼ���.");
                player.setMetadata("permGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                event.setWillClose(true);
            } else if(t == 5) {
                player.setMetadata("itemGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                new MessageHandler().sendMessage(player, "&b�ް� �� �������� Ŭ���� �ּ���. ���� ����Ͻ÷��� �� ������ Ŭ���ϼ���.");
                event.setWillClose(true);
            } else if(t == 6) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a�ް� �� ����ġ�� �Է��ϼ���.(�Ҽ��� ����) ��: 100.00");
                player.setMetadata("permGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                event.setWillClose(true);
            } else if(t == 7) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a�ް� �� ������ �Է��ϼ���.(�Ҽ��� ����) ��: 100.00 - ������ �׳� �ڿ� .0 �ٿ��ֽø� �˴ϴ�.");
                player.setMetadata("permGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menuname, slot, task}));
                event.setWillClose(true);
            } else if(t == 8) {
                new NewTaskSelector().show(menu, player, menuname, slot, task);
            } else if(t == 9) {
                menu.getItemArray().get(slot).delTask(task);
            } else {
                new ItemTaskEditor().show(menu, player, menuname, slot);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("permGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " �޹̼��� ���������� ��ϵǾ����ϴ�!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("permGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new GiveTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("permGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("expGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " ����ġ�� ���������� ��ϵǾ����ϴ�!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("expGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new GiveTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("expGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("levelGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " ������ ���������� ��ϵǾ����ϴ�!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("levelGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu, (String)metadata[0]);
            new GiveTaskEditor().show(menu, e.getPlayer(), (String) metadata[0], (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("levelGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasMetadata("itemGive")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b�������� ���������� �����Խ��ϴ�!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("itemGive").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[0]);
                ItemStack stack = e.getItem();
                menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = ItemStackConverter.convert(stack);
                e.setCancelled(true);
                new GiveTaskEditor().show(menu, e.getPlayer(), (String) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[0], (int) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[1], (int) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[2]);
                e.getPlayer().removeMetadata("itemGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c������ �������Ⱑ ��ҵǾ����ϴ�!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}