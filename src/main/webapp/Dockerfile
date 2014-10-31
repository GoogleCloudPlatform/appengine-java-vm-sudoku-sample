FROM google/appengine-java

# copy the opencv package to the image.
ADD opencv_2.4.10-1-2_all.deb /root/opencv_2.4.10-1-2_all.deb

# install the opencv package with no dependencies.
RUN dpkg --force-depends -i /root/opencv_2.4.10-1-2_all.deb

# fixor the dependencies.
RUN apt-get -y -q --no-install-recommends -f install

# copy the GAE app.
ADD . /app

# small cleanup
RUN rm /app/opencv_2.4.10-1-2_all.deb

# adapt options to be able to bind with jni part of opencv.
ENV CLASSPATH /opt/opencv/share/OpenCV/java/opencv-2410.jar
ENV _JAVA_OPTIONS -Djava.library.path=/opt/opencv/share/OpenCV/java/
