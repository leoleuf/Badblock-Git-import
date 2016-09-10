FROM java:8
MAINTAINER xMalware

# grab gosu for easy step-down from root
RUN apt-get update -y --force-yes && apt-get upgrade -y --force-yes && apt-get dist-upgrade -y --force-yes && apt-get install -y --force-yes screen rsync nano htop nload screen

COPY executor /home

ENTRYPOINT ["/home/executor"]
CMD ["run"]