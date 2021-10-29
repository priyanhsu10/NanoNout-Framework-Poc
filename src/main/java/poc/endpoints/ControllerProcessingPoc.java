package poc.endpoints;

import org.reflections.Reflections;
import sun.reflect.Reflection;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class ControllerProcessingPoc {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("my.project.prefix");

        Set<Class<? extends ControllerBase>> allClasses =
                reflections.getSubTypesOf(ControllerBase.class);
   allClasses.stream().forEach(x-> System.out.println(x.getName()));
    }
}
class EndPointManger{
public  final HashMap<String,EndPoint> RoutDisctionary= new HashMap<>();
public void  AddEndpoint(String route,EndPoint endPoint){
    RoutDisctionary.put( route,endPoint);
}
}
class  EndPoint{
     public  Method ActionMethod;
     public Class<? extends  ControllerBase> controller;
     public HttpMethod httpMethod;
     public  Object[] RouteParameter;
     public Class getReturnType(){
        return ActionMethod.getReturnType();
     }
     public Object Invoke(ControllerBase object){
return  null;
     }

}
 enum  HttpMethod{
    GET,
    POST,
    PUT,
    DELETE
}

