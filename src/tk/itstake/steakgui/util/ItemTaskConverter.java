package tk.itstake.steakgui.util;

import org.bukkit.event.inventory.ClickType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tk.itstake.steakgui.itemtask.ItemTask;

/**
 * Created by bexco on 2015-07-26.
 */
public class ItemTaskConverter {

    public static JSONObject convert(ItemTask task) {
        JSONObject taskjson = new JSONObject();
        JSONArray datajson = new JSONArray();
        for(Object data:task.getData()) {
            datajson.add(data);
        }
        taskjson.put("type", task.getType());
        taskjson.put("clicktype", task.getClickType());
        taskjson.put("data", datajson);
        return taskjson;
    }

    public static ItemTask convert(JSONObject json) {
        String type = (String)json.get("type");
        String clicktype = (String)json.get("clicktype");
        JSONArray data = (JSONArray)json.get("data");
        Object[] dataarray = new String[data.size()];
        int i = 0;
        for(Object d:data) {
            dataarray[i] = (String) d;
            i++;
        }
        if(clicktype.equals("ALL")) {
            return new ItemTask(type, dataarray);
        } else {
            return new ItemTask(type, dataarray, ClickType.valueOf(clicktype));
        }
    }
}
