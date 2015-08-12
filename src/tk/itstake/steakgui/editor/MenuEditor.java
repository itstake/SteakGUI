package tk.itstake.steakgui.editor;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tk.itstake.steakgui.SteakGUI;
import tk.itstake.steakgui.gui.Menu;
import tk.itstake.steakgui.util.MenuFileHandler;

/**
 * Created by ITSTAKE on 2015-08-12.
 */
public class MenuEditor {

    public void show(Menu menu, Player p, String menuName) {
        String title = menu.getTitle();
        if(title.length() > 10) {
            title = ChatColor.stripColor(SteakGUI.convertMessage(menu.getTitle(), menu, p)).substring(0, 11) + "..";
        }
        ItemMenu menuEditor = new ItemMenu(ChatColor.translateAlternateColorCodes('&', "&4수정:&c" + title), ItemMenu.Size.fit(menu.getSize()), (JavaPlugin) Bukkit.getPluginManager().getPlugin("SteakGUI"));
        for(Integer key:menu.getItemArray().keySet()) {
            ItemStack item = menu.getItemArray().get(key).getItemStack();
            String[] lorearray = new String[item.getItemMeta().getLore().size()];
            int i = 0;
            for(String lore:item.getItemMeta().getLore()) {
                lorearray[i] = SteakGUI.convertMessage(lore);
                i++;
            }
            menuEditor.setItem(key, new MenuEditorItem(menu, menuName, p, key, SteakGUI.convertMessage(item.getItemMeta().getDisplayName()), item, lorearray));
        }
        menuEditor.open(p);
    }


    class MenuEditorItem extends MenuItem {
        int t = 0;
        Menu menu = null;
        String menuname = null;
        Player player = null;
        public MenuEditorItem(Menu lmenu, String menuName, Player p, int slot, String displayName, ItemStack icon, String... lore) {
            super(displayName, icon, lore);
            t = slot;
            menu = lmenu;
            menuname = menuName;
            player = p;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if(menu.hasItem(event.getEvent().getSlot())) {
                new ItemEditor().show(menu, player, menuname, event.getEvent().getSlot());
            }
        }
    }
}