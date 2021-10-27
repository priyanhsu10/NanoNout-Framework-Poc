package poc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
interface  IStartup{
    void configureServices();
    void  configurePipeline(PipeBuilder builder);
}

class Startup implements  IStartup{
    public  void configureServices(){
        //ioc builder
    }
    public  void  configurePipeline(PipeBuilder builder){
        builder.addPipe(First.class);
        builder.addPipe(Second.class);
        builder.addPipe((Wrap.class));


    }

}
public class Main {

    public static void main(String[] args) {
        ActionContext actionContext = new ActionContext();
        PipeBuilder builder= new PipeBuilder(Main::first);
        builder.useStartup(Startup.class);
         builder.build().process(actionContext);
//        Action pipeline= new PipeBuilder(Main::first)
//                 .addPipe(Second.class)
//                 .addPipe(TryExecute.class)
//                 .addPipe(Wrap.class)
//                .build();

 //pipeline.process(actionContext);


//        Action action = (act) -> tryExecute2(act,
//                actionContext1 -> tryExecute(actionContext1,
//                        actionContext2 -> wrap(actionContext2,
//                                Main::first)));
//        action.process(actionContext);
   actionContext.getData().entrySet().stream().forEach(x -> System.out.println(x));

    }

    public static void first(ActionContext actionContext) {
        System.out.println("executing first middler ware");
        actionContext.getData().put("first", "first midile ware");
    }

    public static void second(ActionContext actionContext) {
        System.out.println("executing first middler ware");
        actionContext.getData().put("second", "second midile ware");
    }

    public static void wrap(ActionContext actionContext, Action action) {
        System.out.println("starting");
        action.process(actionContext);
        System.out.println("ending");
    }

    public static void tryExecute(ActionContext actionContext, Action action) {
        try {
            System.out.println("trying");
            actionContext.getData().put("tryExecute","try execute");

            action.process(actionContext);
            System.out.println("end try");
        } catch (Exception e) {

        }
    }

    public static void tryExecute2(ActionContext actionContext, Action action) {
        try {
            System.out.println("trying");
            action.process(actionContext);
        } catch (Exception e) {

        }
    }

}


abstract class Pipe {
    protected Action _action;

    public Pipe(Action action) {
        _action = action;
    }

    public abstract void handle(ActionContext actionContext);

}

class Wrap extends Pipe {

    public Wrap(Action action) {
        super(action);
    }

    @Override
    public void handle(ActionContext actionContext) {
        System.out.println("starting wrap");
        actionContext.getData().put("wrap","wrap");


        _action.process(actionContext);
        System.out.println("ending wrap");

    }
}
class  First extends  Pipe{
    public First(Action action) {
        super(action);
    }

    @Override
    public void handle(ActionContext actionContext) {
        System.out.println("starting first middleware");
        actionContext.getData().put("first","first middle waire");
        _action.process(actionContext);
        System.out.println("ending first");

    }
}
class  Second extends  Pipe{
    public Second(Action action) {
        super(action);
    }

    @Override
    public void handle(ActionContext actionContext) {
        System.out.println("starting second");
        _action.process(actionContext);
        System.out.println("ending secondS");

    }
}
class PipeBuilder {
    Action _mainAction;
    List<Class<? extends Pipe>> _pipeTypes;

    public PipeBuilder(Action mainAction) {
        _mainAction = mainAction;
        _pipeTypes = new ArrayList<>();
    }
    public  void useStartup(Class<? extends  IStartup> startup){
        try {
            IStartup startup1 = (IStartup) startup.newInstance();
            startup1.configurePipeline(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public PipeBuilder addPipe(Class<? extends Pipe> pipeType) {
        _pipeTypes.add(pipeType);
        return this;
    }

    public Action createPipe(int index) {
        if (index < _pipeTypes.size()-1) {
            Action childPipeHandler = createPipe(index + 1);
           return build(index, childPipeHandler);

        } else {
            return build(index,null );
        }
    }

    public Action build() {
        return createPipe(0);
    }

    public Action build(int index, Action action) {
        try {
            if (action == null) {
                action = _mainAction;
            }
            Pipe c = _pipeTypes.get(index)
                    .getConstructor(Action.class).newInstance(action);

            return c::handle;
        } catch (InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }
}

class TryExecute extends Pipe {
    public TryExecute(Action action) {
        super(action);
    }

    @Override
    public void handle(ActionContext actionContext) {
        System.out.println("starting tyr");
        _action.process(actionContext);
        System.out.println("ending try");
    }
}

interface Action {
    void process(ActionContext actionContext);

}

class ActionContext {
    private final HashMap<String, Object> data;
    private Object request;
    private Object response;

    public HashMap<String, Object> getData() {
        return data;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    ActionContext() {
        data = new HashMap<>();
    }

}
class  ApplcationBuilder{

}