package poc.endpoints;

import org.reflections.Reflections;
import poc.endpoints.annotations.*;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ControllerProcessingPoc {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("poc.endpoints");

        Set<Class<? extends ControllerBase>> allClasses =
                reflections.getSubTypesOf(ControllerBase.class);
        EndPointManger em = new EndPointManger(allClasses);
        em.endPointMap.entrySet().forEach(x -> System.out.println(x.getKey()));
    }

}

class EndPointManger {
    public Map<String, List<EndPoint>> endPointMap = new HashMap<>();
    private final Set<Class<? extends ControllerBase>> controllerClasses;

    public EndPointManger(Set<Class<? extends ControllerBase>> controllerClasses) {
        this.controllerClasses = controllerClasses;
        process();
    }

    private void process() {
        List<Class<? extends Annotation>> annotations = Arrays.asList(Get.class, Put.class, Delete.class, Post.class);
        for (Class<? extends ControllerBase> controller : controllerClasses) {

            //get base path from route annotation

            Route r = controller.getAnnotation(Route.class);
            String BaseControllerPath = r == null ? "" : r.path();
            prepareEndPoint(controller, BaseControllerPath);


        }

    }

    private void prepareEndPoint(Class<? extends ControllerBase> controller, String BaseControllerPath) {
        Method[] declaredMethods = controller.getDeclaredMethods();
        for (int j = 0, declaredMethodsLength = declaredMethods.length; j < declaredMethodsLength; j++) {
            Method m = declaredMethods[j];

            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }

            EndPoint endPoint = new EndPoint(m, controller);
            if (m.isAnnotationPresent(Get.class)) {
                endPoint.HttpMethod = HttpMethod.GET;
                endPoint.DisplayName = m.getAnnotation(Get.class).path();


            } else if (m.isAnnotationPresent(Post.class)) {
                endPoint.HttpMethod = HttpMethod.POST;
                endPoint.DisplayName = m.getAnnotation(Post.class).path();


            } else if (m.isAnnotationPresent(Put.class)) {
                endPoint.HttpMethod = HttpMethod.PUT;
                endPoint.DisplayName = m.getAnnotation(Put.class).path();

            } else if (m.isAnnotationPresent(Delete.class)) {
                endPoint.HttpMethod = HttpMethod.DELETE;
                endPoint.DisplayName = m.getAnnotation(Delete.class).path();


            }
            String key = BaseControllerPath + endPoint.DisplayName;
            if(key==null|| key.equals("") ){
                continue;
            }
            if (endPointMap.containsKey(key)) {
                endPointMap.get(key).add(endPoint);
            } else {
                ArrayList<EndPoint> endPoints = new ArrayList<>();
                endPoints.add(endPoint);
                endPointMap.put(key, endPoints);
            }


        }
    }

}

class EndPoint {
    public Method ActionMethod;
    public Class<? extends ControllerBase> ControllerClass;
    public HttpMethod HttpMethod;
    public Object[] RouteParameters;
    public String DisplayName;

    public Class getReturnType() {
        return ActionMethod.getReturnType();
    }

    public Object Invoke(ControllerBase object) {
        return null;
    }

    public EndPoint(Method actionMethod, Class<? extends ControllerBase> controlle) {
        ActionMethod = actionMethod;
        this.ControllerClass = ControllerClass;

    }
}

enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE
}

interface methodInterface {


}
