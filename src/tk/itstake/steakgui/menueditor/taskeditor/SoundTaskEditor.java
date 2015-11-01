/*
 * SoundTaskEditor.java
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.menueditor.ItemTaskEditor;
import tk.itstake.steakgui.menueditor.NewTaskSelector;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.itemtask.ItemTask;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class SoundTaskEditor implements Listener {
    public void show(Menu menu, Player player, int slot, int task) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        GUIItem slotItem = menu.getItemArray().get(slot);
        ItemTask edittask = slotItem.getTask(task);
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.TWO_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(0, new ItemTaskItem(menu, player, task, 0, 1,  slot, SteakGUI.convertMessage("&b소리 설정 입력"), Material.JUKEBOX, new String[]{SteakGUI.convertMessage("&b소리의 종류를 입력합니다.")}));
        setting.setItem(1, new ItemTaskItem(menu, player, task, 1, 1,  slot, SteakGUI.convertMessage("&b소리 크기 입력"), Material.NOTE_BLOCK, new String[]{SteakGUI.convertMessage("&b소리의 크기를 입력합니다.")}));
        setting.setItem(2, new ItemTaskItem(menu, player, task, 2, 1,  slot, SteakGUI.convertMessage("&b소리 높낮이 입력"), Material.RECORD_3, new String[]{SteakGUI.convertMessage("&b소리의 높낮이를 입력합니다.")}));
        setting.setItem(9, new ItemTaskItem(menu, player, task, 3, 1,  slot, SteakGUI.convertMessage("&b작업 종류 변경"), Material.ANVIL, new String[]{SteakGUI.convertMessage("&b작업 종류를 변경 합니다.")}));
        setting.setItem(10, new ItemTaskItem(menu, player, task, 4, 1,  slot, SteakGUI.convertMessage("&b작업 삭제"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&b작업을 삭제합니다.")}));
        setting.setItem(11, new ItemTaskItem(menu, player, task, 5, 1,  slot, SteakGUI.convertMessage("&b클릭 방식 변경"), Material.BUCKET, new String[]{SteakGUI.convertMessage("&b클릭 방식을 변경합니다.")}));
        setting.setItem(17, new ItemTaskItem(menu, player, task, 99, 1, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
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
                new MessageHandler().sendMessage(event.getPlayer(), "&a소리 이름을 입력하세요.(목록: https://wiki.itstake.tk/index.php?title=SteakGUI/작업#소리_목록");
                player.setMetadata("soundSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 1) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a소리 크기를 소숫점까지 입력하세요. (예: 1.0)");
                player.setMetadata("volumeSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 2) {
                new MessageHandler().sendMessage(event.getPlayer(), "&a소리 높낮이를 소숫점까지 입력하세요. (예: 1.0)");
                player.setMetadata("pitchSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), slot, task}));
                event.setWillClose(true);
            } else if(t == 3) {
                new NewTaskSelector().show(menu, player, slot, task);
            } else if(t == 4) {
                menu.getItemArray().get(slot).delTask(task);
                MenuFileHandler.saveMenu(menu);
                new ItemTaskEditor().show(MenuFileHandler.loadMenu(menu.getName()), player, slot);
            } else if(t == 5) {
                new TaskClickTypeEditor().show(menu, player, slot, task);
            } else {
                new ItemTaskEditor().show(menu, player, slot);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("soundSet")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 소리 이름이 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("soundSet").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[0] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new SoundTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("soundSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("volumeSet")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 소리 크기가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("volumeSet").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[1] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new SoundTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("volumeSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        } else if(e.getPlayer().hasMetadata("pitchSet")) {
            new MessageHandler().sendMessage(e.getPlayer(), "&a" + e.getMessage() + " 소리 높낮이가 성공적으로 등록되었습니다!");
            Object[] metadata = (Object[]) e.getPlayer().getMetadata("pitchSet").get(0).value();
            Menu menu = MenuFileHandler.loadMenu((String) metadata[0]);
            menu.getItemArray().get((int)metadata[1]).getTask((int)metadata[2]).getData()[2] = e.getMessage();
            MenuFileHandler.saveMenu(menu);
            new SoundTaskEditor().show(menu, e.getPlayer(), (int) metadata[1], (int) metadata[2]);
            e.setCancelled(true);
            e.getPlayer().removeMetadata("pitchSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
        }
    }
}
