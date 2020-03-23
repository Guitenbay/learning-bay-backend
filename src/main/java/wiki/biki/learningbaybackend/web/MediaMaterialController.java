package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.MediaMaterial;
import wiki.biki.learningbaybackend.service.MediaMaterialService;
import wiki.biki.learningbaybackend.service.impl.MediaMaterialServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/media-material")
public class MediaMaterialController {
    private MediaMaterialService mediaMaterialService = new MediaMaterialServiceImpl();

    @GetMapping("/all")
    public String getMediaMaterials() {
        ArrayList<MediaMaterial> mediaMaterialList = mediaMaterialService.getMediaMaterialList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", mediaMaterialList);
        return json.toJSONString();
    }

    @GetMapping
    public String getMediaMaterial(String uri) {
        MediaMaterial mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", mediaMaterial);
        return json.toJSONString();
    }

    @PutMapping
    public String insertMediaMaterial(@RequestBody MediaMaterial mediaMaterial) {
        boolean result = mediaMaterialService.insert(mediaMaterial);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateMediaMaterial(@RequestBody MediaMaterial mediaMaterial) {
        boolean result = mediaMaterialService.update(mediaMaterial);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @DeleteMapping
    public String deleteMediaMaterial(String uri) {
        boolean result = mediaMaterialService.delete(uri);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

}