FROM nginx

EXPOSE 80

COPY dist/ /usr/share/nginx/html/

ENTRYPOINT exec /usr/sbin/nginx -g 'daemon off;'
