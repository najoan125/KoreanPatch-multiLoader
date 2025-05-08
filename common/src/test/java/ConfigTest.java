import com.google.gson.Gson;
import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryIndicator;

public class ConfigTest {
    public static void main(String[] args) {
        ModConfig config = new ModConfig();
        Gson gson = new Gson();
        String json = gson.toJson(config);
        System.out.println(json);

        String json2 = "{\"categoryIndicator\":{\"showIndicator\":true,\"outline\":{\"showOutline\":true,\"rounded\":true,\"colorOpacity\":{\"koreanColor\":16711680,\"enColor\":65280,\"imeColor\":16777215,\"opacity\":100}},\"background\":{\"koreanColor\":0,\"enColor\":0,\"imeColor\":0,\"opacity\":50},\"text\":{\"koreanColor\":16777215,\"enColor\":16777215,\"imeColor\":16777215,\"opacity\":100},\"animation\":{\"showAnimation\":false,\"speed\":30}}}";
        ModConfig config2 = gson.fromJson(json2, ModConfig.class);
        CategoryIndicator indicator = config2.getCategoryIndicator();
        indicator.setShowIndicator(false);
        String json3 = gson.toJson(config2);
        System.out.println(json3);
    }
}
