package poc.endpoints.controllers;

import poc.endpoints.ControllerBase;
import poc.endpoints.annotations.Delete;
import poc.endpoints.annotations.Get;
import poc.endpoints.annotations.Post;
import poc.endpoints.annotations.Put;

public class HelloController  extends ControllerBase {
    @Get(path = "/hello")
    public String get() {
        return "welcome";
    }
    @Post(path = "/hello")
    public String post(String name){
        return "this is post "+name;
    }
    @Put(path = "/helloupdate")
    public String put(String name){
        return "this is post "+name;
    }
    @Delete(path = "/helloDelete")
    public String delete(String name){
        return "this is post "+name;
    }

}
