/*
 * ItemEditor.java
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

package tk.itstake.steakgui.menueditor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.GUIItem;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.MenuFileHandler;
import tk.itstake.util.MessageHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class ItemEditor implements Listener {
    public void show(Menu menu, Player player, int slot) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, player)).substring(0, 11) + "";
        }
        ItemStack slotItem = menu.getItemArray().get(slot).getItemStack();
        ItemMenu setting = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.THREE_LINE, (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        setting.setItem(9, new ItemEditorItem(menu, player, 0, slot, SteakGUI.convertMessage("&b손에서 아이템 가져오기"), Material.HOPPER, new String[]{SteakGUI.convertMessage("&b손에서 아이템을 가져옵니다.")}));
        setting.setItem(11, new ItemEditorItem(menu, player, 1, slot, SteakGUI.convertMessage("&b아이템 삭제"), Material.NETHER_BRICK_ITEM, new String[]{SteakGUI.convertMessage("&c아이템을 삭제합니다.")}));
        String permission = menu.getItemArray().get(slot).getPermission();
        if(permission.equals("")) {
            permission = "없음";
        }
        setting.setItem(13, new ItemEditorItem(menu, player, 2, slot, SteakGUI.convertMessage("&b펄미션 설정"), Material.REDSTONE_TORCH_ON, new String[]{SteakGUI.convertMessage("&c현재 설정된 펄미션:" + permission), SteakGUI.convertMessage("&b어떤 펄미션이 있어야 보일지 설정합니다."), SteakGUI.convertMessage("&3<클릭> 으로 펄미션 설정"), SteakGUI.convertMessage("&2<버리기 키> 로 설정된 펄미션 삭제")}));
        setting.setItem(15, new ItemEditorItem(menu, player, 3, slot, SteakGUI.convertMessage("&c작업 설정"), Material.REDSTONE_BLOCK, new String[]{SteakGUI.convertMessage("&c이 아이템의 작업을 설정합니다.")}));
        setting.setItem(17, new ItemEditorItem(menu, player, 4, slot, SteakGUI.convertMessage("&c아이템 정보 설정"), Material.ENCHANTED_BOOK, new String[]{SteakGUI.convertMessage("&c아이템의 이름과 로어를 설정합니다.")}));
        setting.setItem(26, new ItemEditorItem(menu, player, 5, slot, SteakGUI.convertMessage("&c돌아가기"), Material.FEATHER, new String[]{SteakGUI.convertMessage("&c이전 매뉴로 돌아갑니다.")}));
        setting.open(player);
    }

    class ItemEditorItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        Player player = null;
        int s = 0;
        public ItemEditorItem(Menu lmenu, Player p, int type, int slot, String displayName, Material icon, String... lore) {
            super(displayName, new ItemStack(icon), lore);
            t = type;
            s = slot;
            menu = lmenu;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(t == 0) {
                player.setMetadata("itemChange", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), s}));
                new MessageHandler().sendMessage(player, "&b변경할 아이템을 클릭해 주세요. 만약 변경할 아이템이 없으시다면 빈 슬롯을 클릭하세요.");
                event.setWillClose(true);
            } else if(t == 1) {
                menu.removeItem(s);
                new MessageHandler().sendMessage(player, "&c아이템이 성공적으로 삭제되었습니다!");
                MenuFileHandler.saveMenu(menu);
                menu = MenuFileHandler.loadMenu(menu.getName(), true);
                new MenuEditor().show(menu, player);
            } else if(t == 2) {
                if(!(event.getClick().equals(ClickType.DROP) || event.getClick().equals(ClickType.CONTROL_DROP))) {
                    player.setMetadata("permSet", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SteakGUI"), new Object[]{menu.getName(), s}));
                    new MessageHandler().sendMessage(player, "&b펄미션을 입력해 주세요. 취소하시려면 'cancel' 혹은 '취소' 를 입력하세요.");
                    event.setWillClose(true);
                } else {
                    new MessageHandler().sendMessage(player, "&c펄미션이 삭제되었습니다.");
                    menu.getItemArray().get(s).setPermission("");
                    MenuFileHandler.saveMenu(menu);
                    menu = MenuFileHandler.loadMenu(menu.getName(), true);
                    new ItemEditor().show(menu, player, s);
                }
            } else if(t == 3) {
                new ItemTaskEditor().show(menu, player, s);
            } else if(t == 4) {
                new ItemStackEditor().show(menu, player, s);
            } else if(t == 5) {
                new MenuEditor().show(menu, player);
            }
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasMetadata("itemChange")) {
            if(e.getItem() != null) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b아이템을 성공적으로 가져왔습니다!");
                Menu menu = MenuFileHandler.loadMenu((String) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[0], true);
                ItemStack s = e.getItem();
                ItemStack stack = new ItemStack(s.getType(), s.getAmount(), s.getDurability());
                stack.setData(s.getData());
                stack.setItemMeta(s.getItemMeta());
                menu.setItem((int) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[1], new GUIItem(stack, "", menu.getItemArray().get((int) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[1]).getTasks()));
                MenuFileHandler.saveMenu(menu);
                e.setCancelled(true);
                menu = MenuFileHandler.loadMenu(menu.getName(), true);
                new ItemEditor().show(menu, e.getPlayer(), (int) ((Object[]) e.getPlayer().getMetadata("itemChange").get(0).value())[1]);
                e.getPlayer().removeMetadata("itemChange", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c아이템 가져오기가 취소되었습니다!");
                e.setCancelled(true);
                e.getPlayer().removeMetadata("itemChange", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasMetadata("permSet")) {
            if(!e.getMessage().equals("cancel") && !e.getMessage().equals("취소")) {
                new MessageHandler().sendMessage(e.getPlayer(), "&b펄미션이 " + e.getMessage() + " 로 설정되었습니다.");
                e.setCancelled(true);
                Menu menu = MenuFileHandler.loadMenu((String)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[0], true);
                menu.getItemArray().get((int)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[1]).setPermission(e.getMessage());
                MenuFileHandler.saveMenu(menu);
                menu = MenuFileHandler.loadMenu(menu.getName(), true);
                new ItemEditor().show(menu, e.getPlayer(), (int)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[1]);
                e.getPlayer().removeMetadata("permSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            } else {
                new MessageHandler().sendMessage(e.getPlayer(), "&c취소되었습니다.");
                e.setCancelled(true);
                new MenuSetting().show(MenuFileHandler.loadMenu((String)((Object[])e.getPlayer().getMetadata("permSet").get(0).value())[0], true), e.getPlayer());
                e.getPlayer().removeMetadata("permSet", Bukkit.getPluginManager().getPlugin("SteakGUI"));
            }
        }
    }
}
