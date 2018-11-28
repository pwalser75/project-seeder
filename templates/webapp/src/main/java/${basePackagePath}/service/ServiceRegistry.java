package ${basePackage}.service;

import ${basePackage}.api.NotesService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A simple service registry.
 *
 * @author wap
 * @since 28.11.2018
 */
public class ServiceRegistry {

    private static ServiceRegistry instance;

    private final Map<Class<?>, Object> services = new HashMap<>();

    private ServiceRegistry() {
        services.put(NotesService.class, new NotesServiceImpl());
    }

    public static synchronized ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    public <S> S get(Class<S> serviceClass) {
        S service = serviceClass.cast(services.get(serviceClass));
        if (service == null) {
            throw new NoSuchElementException("Service not found: " + serviceClass.getName());
        }
        return service;
    }
}
