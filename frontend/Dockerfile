# --- Build-Stage ---
FROM node:20-alpine AS build
WORKDIR /app

# Erst package.json (Layer-Caching für npm install)
COPY package.json package-lock.json ./
RUN npm install

# Rest kopieren und bauen
COPY . .
RUN npm run build

# --- Runtime-Stage ---
FROM nginx:alpine AS runtime

# Nginx-Config einsetzen
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Gebaute statische Dateien aus der Build-Stage
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80