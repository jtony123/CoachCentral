package security;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;

@Singleton
public class MyHandlerCache implements HandlerCache {

    private final DeadboltHandler defaultHandler;
    private final Map<String, DeadboltHandler> handlers = new HashMap<>();

    @Inject
    public MyHandlerCache(final ExecutionContextProvider ecProvider)
    {
    	defaultHandler = new MyDeadboltHandler(ecProvider);
        handlers.put(HandlerKeys.DEFAULT.key, defaultHandler);
//        handlers.put(HandlerKeys.ALT.key, new MyAlternativeDeadboltHandler());
//        handlers.put(HandlerKeys.BUGGY.key, new BuggyDeadboltHandler());
//        handlers.put(HandlerKeys.NO_USER.key, new NoUserDeadboltHandler());
    }

    @Override
    public DeadboltHandler apply(final String key)
    {
        return handlers.get(key);
    }

    @Override
    public DeadboltHandler get()
    {
        return defaultHandler;
    }
}
