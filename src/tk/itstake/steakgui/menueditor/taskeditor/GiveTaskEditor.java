/*
 * GiveTaskEditor.java
 *
 * Copyright (c) 2015 ITSTAKE
 *
 * This program is free software: you can redistribute it and/or modify
 *
 * it under the terms of the GNU General Public License as published by
 *
 * the Free Software Foundation, either version 3 of the License, or
 *
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tk.itstake.steakgui.menueditor.taskeditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.menueditor.ItemTaskEditor;
import tk.itstake.steakgui.menueditor.NewTaskSelector;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.ItemStackConverter;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class GiveTaskEditor implements Listener {
    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, player, task, 0, 1,  slot, SteakGUI.convertMessage("&c펄미션 받기"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b받는 물품을 펄미션으로 설정합니다.")}));
        setting.setItem(1, new ItemTaskItem(menu, player, task, 1, 1,  slot, SteakGUI.convertMessage("&c아이템 받기"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b받는 물품을 아이템으로 설정합니다.")}));
        setting.setItem(2, new ItemTaskItem(menu, player, task, 2, 1,  slot, SteakGUI.convertMessage("&c경험치 받기"), Material.ENCHANTMENT_TABLE, new String[]{SteakGUI.convertMessage("&b받는 물품을 경험치로 설정합니다.")}));
        setting.setItem(3, new ItemTaskItem(menu, player, task, 3, 1,  slot, SteakGUI.convertMessage("&c레벨 받기"), Material.EXP_BOTTLE, new String[]{SteakGUI.convertMessage("&b받는 물품을 레벨으로 설정합니다.")}));
        setting.setItem(3, new ItemTaskItem(menu, player, task, 4, 1,  slot, SteakGUI.convertMessage("&c돈 받기"), Material.MAP, new String[]{SteakGUI.convertMessage("&b받는 물품을 돈으로 설정합니다.")}));
        if(((String)edittask.getData()[0]).equals("permission")) {
            setting.setItem(9, new ItemTaskItem(menu, player, task, 5, 1,  slot, SteakGUI.convertMessage("&b펄미션 입력"), Material.PAPER, new String[]{SteakGUI.convertMessage("&b받게 될 펄미션을 입력합니다.")}));
        } else if(((String)edittask.getData()[0]).equals("item")) {
            setting.setItem(9, new ItemTaskItem(menu, player, task, 6, 1,  slot, SteakGUI.convertMessage("&b아이템 선택"), Material.EMERALD, new String[]{SteakGUI.convertMessage("&b받게 될 아이템을 손에서 선택합니다.")}));
        } else if(((String)edittask.getData()[0]).equals("exp")) {
            setting.setItem(9, new ItemTaskItem(menu, player, task, 7, 1,  slot, SteakGUI.convertMessage("&b경험치 입력"), Material.ENCHANTMENT_TABLE, new String[]{SteakGUI.convertMessage("&b받게 될 경험치를 입력합니다.")}));
        } else if(((String)edittask.getData()[0]).equals("level")) {
            setting.setItem(9, new ItemTaskItem(menu, player, task, 8, 1,  slot, SteakGUI.convertMessage("&b레벨 입력"), Material.EXP_BOTTLE, new String[]{SteakGUI.convertMessage("&b받게 될 레벨을 입력합니다.")}));
        } else if(((String)edittask.getData()[0]).equals("money")) {
            setting.setItem(9, new ItemTaskItem(menu, player, task, 9, 1,  slot, SteakGUI.convertMessage("&b돈 입력"), Material.MAP, new String[]{SteakGUI.convertMessage("&b받게 될 돈을 입력합니다.")}));
        }
        setting.setItem(18, new ItemTaskItem(menu, player, task, 10, 1,  slot, SteakGUI.convertMessage("&b작업 종류 변경"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b작업 종류를 변경 합니다.")}));
        setting.setItem(19, new ItemTaskItem(menu, player, task, 11, 1,  slot, SteakGUI.convertMessage("&b작업 삭제"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&b작업을 삭제합니다.")}));
        setting.setItem(20, new ItemTaskItem(menu, player, task, 12, 1,  slot, SteakGUI.convertMessage("&b클릭 방식 변경"), Material.BUCKET, new String[]{SteakGUI.convertMessage("&b클릭 방식을 변경합니다.")}));
        setting.setItem(26, new ItemTaskItem(menu, player, task, 99, 1, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemTaskItem extends MenuItem {
        int t = 0;
        int task = 0;
        int slot = 0;
        Menu menu = null;
        Player player = null;
        public ItemTaskItem(Menu lmenu, Player p, int ltask, int type, int amount, int s, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon, amount), lore);
            slot = s;
            t = type;
            task = ltask;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            ItemTask editTask = menu.getItemArray().get(slot).getTask(task);
            if(t == 0) {
                editTask.getData()[0] = "permission";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 펄미션을 받게 됩니다.");
                MenuFileHandler.saveMenu(menu);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(t == 1) {
                editTask.getData()[0] = "item";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 아이템을 받게 됩니다.");
                MenuFileHandler.saveMenu(menu);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(t == 2) {
                editTask.getData()[0] = "exp";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 경험치를 받게 됩니다.");
                MenuFileHandler.saveMenu(menu);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(t == 3) {
                editTask.getData()[0] = "level";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 레벨을 받게 됩니다.");
                MenuFileHandler.saveMenu(menu);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(t == 4) {
                editTask.getData()[0] = "money";
                new MessageHandler().sendMessage(event.getPlayer(), "&c이제 돈을 받게 됩니다.");
                MenuFileHandler.saveMenu(menu);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), event.getPlayer(), slot, task);
            } else if(t == 5) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a받게 될 펄미션을 입력하세요.");
                player.setMetadata("permGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 6) {
                player.setMetadata("itemGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                new MessageHandler().sendMessage(player, "&b받게 될 아이템을 클릭해 주세요. 만약 취소하시려면 빈 슬롯을 클릭하세요.");
                event.setWillClose(true);
            } else if(t == 7) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a받게 될 경험치를 입력하세요.(소숫점 불가) 예: 100");
                player.setMetadata("expGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 8) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a받게 될 레벨을 입력하세요.(소숫점 불가) 예: 10");
                player.setMetadata("levelGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 9) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a받게 될 돈을 입력하세요.(소숫점 포함) 예: 100.0, 100");
                player.setMetadata("moneyGive", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 10) {
                new NewTaskSelector().show(menu, player, slot, task);
            } else if(t == 11) {
                menu.getItemArray().get(slot).delTask(task);
                MenuFileHandler.saveMenu(menu);
                new ItemTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), player, slot);
            } else if(t == 12) {
                new TaskClickTypeEditor().show(menu, player, slot, task);
            } else {
                new ItemTaskEditor().show(menu, player, slot);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("permGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 펄미션이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("permGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new GiveTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("permGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("expGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 경험치가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("expGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new GiveTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("expGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("levelGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 레벨이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("levelGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new GiveTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("levelGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("moneyGive")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("moneyGive").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0], true);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new GiveTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("moneyGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasMetadata("itemGive")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b아이템을 성공적으로 가져왔습니다!");
                Object[] metadata = (Object[]) e.getPlayer().getMetadata("itemGive").get(0).value();
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[0]);
                ItemStack stack = e.getItem();
                menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = ItemStackConverter.convert(stack).toJSONString();
                MenuFileHandler.saveMenu(menu);
                e.setCancelled(true);
                new GiveTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), e.getPlayer(), (int) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[1], (int) ((Object[]) e.getPlayer().getMetadata("itemGive").get(0).value())[2]);
                e.getPlayer().removeMetadata("itemGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c아이템 가져오기가 취소되었습니다!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemGive", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}
