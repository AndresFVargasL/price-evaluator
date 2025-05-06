FROM eclipse-temurin:21-jdk-alpine

# Crear un usuario sin privilegios
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Crear el directorio de la app y dar permisos
RUN mkdir -p /app && chown -R appuser:appgroup /app

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el jar a la imagen
COPY --chown=appuser:appgroup applications/app-service/build/libs/*.jar price-evaluator.jar

# Cambiar a usuario no-root
USER appuser

# Ejecutar la aplicación
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar", "price-evaluator.jar"]
