FROM debian:wheezy

# base system
RUN apt-get -q update; apt-get install --no-install-recommends -y -q ca-certificates && apt-get -y -q upgrade

# devtools
RUN apt-get -y -q --no-install-recommends install build-essential unzip cmake wget alien

# opencv deps

# python
RUN apt-get -y -q --no-install-recommends install python-dev python-numpy
# java
RUN apt-get -y -q --no-install-recommends install openjdk-7-jdk ant
# images
RUN apt-get -y -q --no-install-recommends install libgtk2.0-dev libjpeg-dev libpng-dev libtiff-dev libjasper-dev
# video
RUN apt-get -y -q --no-install-recommends install libavcodec-dev libavformat-dev libswscale-dev

WORKDIR /root
RUN wget http://downloads.sourceforge.net/project/opencvlibrary/opencv-unix/2.4.10/opencv-2.4.10.zip && unzip opencv-2.4.10.zip

# prepare a directory for the prefix
RUN mkdir /opt/opencv

# make opencv
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64
RUN cd opencv-2.4.10 && mkdir release && cd release && cmake -D CMAKE_BUILD_TYPE=RELEASE -D CMAKE_INSTALL_PREFIX=/opt/opencv -DJAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jdk -D JAVA_INCLUDE_PATH=/usr/lib/jvm/java-7-openjdk-amd64/include -D JAVA_INCLUDE_PATH2=/usr/lib/jvm/java-7-openjdk-amd64/include/linux .. && make -j12 install

# create a package out of the prefix
RUN tar cpzvf /root/opencv-2.4.10.tgz /opt/opencv && alien --to-deb --description="Home brewed opencv package" --version=2.4.10-1 opencv-2.4.10.tgz

# The output will be in /root/opencv_2.4.10-1-2_all.deb
