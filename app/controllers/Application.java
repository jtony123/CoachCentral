package controllers;

import play.*;
import play.mvc.*;
//import play.db.jpa.*;
import views.html.*;
import models.Person;
import play.data.FormFactory;
import javax.inject.Inject;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.data.*;
import static play.data.Form.*;

import static play.libs.Json.*;

public class Application extends Controller {

    @Inject
    FormFactory formFactory;

    public Result index() {
        return ok(index.render());
    }


    public Result addPerson() {
        Person person = formFactory.form(Person.class).bindFromRequest().get();
        person.save();
        return redirect(routes.Application.index());
    }

    public Result getPersons() {
        List<Person> persons = Person.find.all();
        return ok(toJson(persons));
    }
}
