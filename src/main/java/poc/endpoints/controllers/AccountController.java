package poc.endpoints.controllers;

import poc.endpoints.ControllerBase;
import poc.endpoints.annotations.*;

@Route(path = "/account")
public class AccountController extends ControllerBase {
    @Get
    public String get() {
        return "welcome";
    }
    @Post
    public String post(String name){
        return "this is post "+name;
    }
    @Put
    public String put(String name){
        return "this is post "+name;
    }
    @Delete
    public String delete(String name){
        return "this is post "+name;
    }
}
