package wiki.biki.learningbaybackend.fuseki;

import org.apache.jena.rdf.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FusekiUtils {

    public static <T> T createEntityFromModel(Class<T> clazz, Model model) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 创建新实例
        T entity = clazz.getDeclaredConstructor().newInstance();

        Map<String, ArrayList<String>> valueMap = new HashMap<>();
        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            String key = statement.getPredicate().getLocalName();
            if (valueMap.containsKey(key)) {
                valueMap.get(key).add(statement.getObject().toString());
            } else {
                ArrayList<String> values = new ArrayList<>();
                values.add(statement.getObject().toString());
                valueMap.put(key, values);
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            // 设置该属性可访问
            field.setAccessible(true);
            FusekiProperty property = field.getAnnotation(FusekiProperty.class);
            if (property == null) continue;
            ArrayList<String> values = valueMap.get(property.value());
            if (values == null) continue;
            if (values.size() == 1) {
                field.set(entity, values.get(0));
            } else {
                field.set(entity, values);
            }
        }
        return entity;
    }

    public static <T> ArrayList<T> createEntityListFromModel(Class<T> clazz, Model model) {
        ResIterator resIterator = model.listSubjects();
        Map<Resource, Map<String, ArrayList<String>>> subjects = new HashMap<>();
        while (resIterator.hasNext()) {
            subjects.put(resIterator.nextResource(), new HashMap<>());
        }
        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.nextStatement();
            // 若 stmt 主体不存在 则 继续（逻辑上不可能有这种情况）
            if (!subjects.containsKey(statement.getSubject())) continue;
            Map<String, ArrayList<String>> valueMap = subjects.get(statement.getSubject());
            String key = statement.getPredicate().getLocalName();
            if (valueMap.containsKey(key)) {
                valueMap.get(key).add(statement.getObject().toString());
            } else {
                ArrayList<String> values = new ArrayList<>();
                values.add(statement.getObject().toString());
                valueMap.put(key, values);
            }
        }
        // 创建 entity
        ArrayList<T> entities = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        subjects.forEach((_uri_, valueMap) -> {
            T entity = null;
            try {
                entity = clazz.getDeclaredConstructor().newInstance();
                for (Field field: fields) {
                    // 设置该属性可访问
                    field.setAccessible(true);
                    FusekiProperty property = field.getAnnotation(FusekiProperty.class);
                    if (property == null) {
                        // 设置 uri
                        if (field.getName().equals("uri")) field.set(entity, _uri_.toString());
                        continue;
                    }
                    ArrayList<String> values = valueMap.get(property.value());
                    if (values == null) continue;
                    if (values.size() == 1) {
                        field.set(entity, values.get(0));
                    } else {
                        field.set(entity, values);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            entities.add(entity);
        });
        return entities;
    }

}
