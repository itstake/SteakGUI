/*
 * UpdateChecker.java
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

package tk.itstake.steakgui.util;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.itstake.util.MessageHandler;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ITSTAKE on 2015-08-18.
 */
public class UpdateChecker implements Listener {
    private boolean isNotRecent = false;
    private String newversion = null;
    public void updateCheck() {
        try {
            URL url = new URL("http://update.itstake.tk/SteakGUI.txt");
            Scanner s = new Scanner(url.openStream());
            String serverversion = "";
            while (s.hasNextLine()) {
                serverversion = serverversion + s.nextLine();
            }
            if(!serverversion.equals(Bukkit.getPluginManager().getPlugin("SteakGUI").getDescription().getVersion().toString())) {
                isNotRecent = true;
                newversion = serverversion;
                new MessageHandler().sendConsoleMessage("&aSteakGUI 의 최신 버전이 출시되었습니다!\n새로운 버전:" + newversion + "\n&b다운로드 하시려면 http://wiki.itstake.tk/index.php?title=SteakGUI 에 접속하세요!");
            } else {
                new MessageHandler().sendConsoleMessage("&aSteakGUI 는 현재 최신 버전입니다.");
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(e.getPlayer().isOp()) {
            try {
                URL url = new URL("http://update.itstake.tk/SteakGUI.txt");
                Scanner s = new Scanner(url.openStream());
                String serverversion = "";
                while (s.hasNextLine()) {
                    serverversion = serverversion + s.nextLine();
                }
                if(!serverversion.equals(Bukkit.getPluginManager().getPlugin("SteakGUI").getDescription().getVersion().toString())) {
                    isNotRecent = true;
                    newversion = serverversion;
                    new MessageHandler().sendMessage(e.getPlayer(), "&aSteakGUI 의 최신 버전이 출시되었습니다!\n새로운 버전:" + newversion + "\n&b다운로드 하시려면 http://wiki.itstake.tk/index.php?title=SteakGUI 에 접속하세요!");
                } else {
                    new MessageHandler().sendMessage(e.getPlayer(), "&aSteakGUI 는 현재 최신 버전입니다.");
                }
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
