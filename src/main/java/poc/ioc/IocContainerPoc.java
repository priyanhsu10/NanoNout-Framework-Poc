package poc.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
//some use case will be implement like
//1 . cycle detection
//2. Maintaining singleton and scope abjects cashed(as per requested)
//3.transient will follow the corrent flow
public class IocContainerPoc {
    public static void main(String[] args) {
        Container c= new Container();
        c.addSington(IA.class,A.class);
        c.addSington(IB.class,B.class);
       //validatin check
        // c.addSington(IA.class,IA.class);

  IB a=c.resolve(IB.class);
  a.test();
    }
}

interface IA {
    void test();
}

class A implements IA {
    public void test() {
        System.out.println("testing A");
    }
}
interface IB{
    void test();
}
class B implements IB{

    private final IA ia;

    public B(IA ia) {
        this.ia = ia;
    }

    @Override
    public void test() {
        ia.test();
        System.out.println("b class method");
    }
}
class Container {
    public final HashMap<Class, Descriptor> tank = new HashMap<>();

    public <Source> void addTransient(Class<Source> source, Class<? extends Source> target) {
        validate(target);
        Descriptor discripter = new Descriptor(source, target, Scope.Transient);
        tank.put(source, discripter);
    }

    public <Source> void addSington(Class<Source> source, Class<? extends Source> target) {
validate(target);
        Descriptor discripter = new Descriptor(source, target, Scope.Singleton);
        tank.put(source, discripter);
    }
    private void validate(Class target){
        if(Modifier.isAbstract(target.getModifiers())){
            throw  new RuntimeException("Invalid type to register");
        }
    }

    public <Source> void addScope(Class<Source> source, Class<? extends Source> target) {
        Descriptor discripter = new Descriptor(source, target, Scope.Singleton);
        tank.put(source, discripter);
    }

    public <T> T resolve(Class<T> source) {
        if (tank.containsKey(source)) {
            Descriptor discripter = tank.get(source);
            return source.cast(createInstance(discripter));
        }
       else  {
            throw new RuntimeException("Type Not regitser in container");
        }
    }

    public Object createInstance(Descriptor discripter) {

        Optional<Constructor<?>> constructor = Arrays.stream(discripter.getImplemetor().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount));

        if(!constructor.isPresent()){
            Object o = null;
            try {
                o = discripter.getImplemetor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return o;
        }
        Class[] typeParameters = constructor.get().getParameterTypes();
        Object[] paramerters = Arrays.stream(typeParameters).map(y -> resolve(y)).toArray();

        try {
            Object o = discripter.getImplemetor()
                    .getConstructor(typeParameters)
                    .newInstance(paramerters);
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

return null;
    }


}

class Descriptor {
    private Class<? extends Object> source;
    private Class<? extends Object> implemetor;
    private Scope schope;

    public Class<?> getSource() {
        return source;
    }

    public Class<?> getImplemetor() {
        return implemetor;
    }

    public Scope getSchope() {
        return schope;
    }

    public Descriptor(Class<? extends Object> source, Class<? extends Object> implemetor) {
        this.source = source;
        this.implemetor = implemetor;
        this.schope = Scope.Singleton;
    }

    public Descriptor(Class<? extends Object> source, Class<? extends Object> implemetor, Scope schope) {
        this.source = source;
        this.implemetor = implemetor;
        this.schope = schope;
    }
}

enum Scope {
    Transient,
    RequestScope,
    Singleton
}