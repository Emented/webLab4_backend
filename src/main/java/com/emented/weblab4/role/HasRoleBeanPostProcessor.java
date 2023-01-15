package com.emented.weblab4.role;

import com.emented.weblab4.exception.UserDoesntHaveRoleException;
import com.emented.weblab4.model.Role;
import com.emented.weblab4.model.User;
import com.emented.weblab4.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HasRoleBeanPostProcessor implements BeanPostProcessor {

    private final Map<Class<?>, Map<Method, String[]>> classToHasRoleAnnotatedMethodsMap;

    private final UserRepository userRepository;

    @Autowired
    public HasRoleBeanPostProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.classToHasRoleAnnotatedMethodsMap = new HashMap<>();
    }


    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean,
                                                  @NonNull String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(RestController.class)
                || bean.getClass().isAnnotationPresent(Controller.class)) {

            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(HasRole.class)) {
                    classToHasRoleAnnotatedMethodsMap.compute(bean.getClass(), (k, v) -> {
                        if (v == null) {
                            Map<Method, String[]> newMethodMap = new HashMap<>();
                            newMethodMap.put(method, method.getAnnotation(HasRole.class).value());
                            return newMethodMap;
                        }
                        v.put(method, method.getAnnotation(HasRole.class).value());
                        return v;
                    });
                }
            }

        }


        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean,
                                                 @NonNull String beanName) throws BeansException {


        if (classToHasRoleAnnotatedMethodsMap.containsKey(bean.getClass())) {
            Map<Method, String[]> methodToAnnotaionValueMap = classToHasRoleAnnotatedMethodsMap.get(bean.getClass());

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());

            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {

                log.info("Started proxy for method: {}", method.getName());

                UserDetails userDetails = null;
                for (Object arg : args) {
                    if (UserDetails.class.isAssignableFrom(arg.getClass())) {
                        log.info("Found user details for method: {}", method.getName());
                        userDetails = (UserDetails) arg;
                        break;
                    }
                }
                if (userDetails != null) {
                    log.info("Trying to find user for method: {}", method.getName());
                    Optional<User> userOptional = userRepository.findUserByEmail(userDetails.getUsername());

                    if (userOptional.isPresent()) {

                        log.info("Found user for method: {}", method.getName());

                        User user = userOptional.get();
                        Set<String> userRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

                        for (String role : methodToAnnotaionValueMap.get(method)) {
                            if (userRoles.contains(role)) {
                                return proxy.invokeSuper(obj, args);
                            }
                        }

                        throw new UserDoesntHaveRoleException("User with email: " + user.getEmail() + " doesn't have access to this method!");
                    }
                }
                log.info("User details not found for method: {}", method.getName());
                return proxy.invokeSuper(obj, args);
            });

            Field[] fields = bean.getClass().getDeclaredFields();

            Class<?>[] argumentClasses = new Class[fields.length];
            Object[] arguments = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                try {
                    Object argument = field.get(bean);

                    arguments[i] = argument;
                    argumentClasses[i] = field.getType();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }

            bean = enhancer.create(argumentClasses, arguments);
        }

        return bean;
    }

}
