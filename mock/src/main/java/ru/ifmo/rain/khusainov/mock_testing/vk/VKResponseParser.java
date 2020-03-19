package ru.ifmo.rain.khusainov.mock_testing.vk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class VKResponseParser {

    public List<VKInfo> parseResponse(String response) {
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
        JsonArray entries = jsonResponse.getAsJsonObject("response").getAsJsonArray("items");
        List<VKInfo> infos = new ArrayList<>(entries.size());
        for (JsonElement e : entries) {
            JsonObject d = (JsonObject) e;
            infos.add(new VKInfo(
                    d.get("id").getAsInt(),
                    d.get("owner_id").getAsInt(),
                    d.get("date").getAsInt()));
        }
        return infos;
    }
}
