#!bin/sh
BASE_PATH="/home/ubuntu/hunsu/s04p13c102"

# master pull
cd $BASE_PATH
git pull origin master

# 인증서버 빌드 및 배포
cd $BASE_PATH"/authserver"
chmod 755 gradlew
./gradlew build
cd $BASE_PATH"/authserver/build/libs"
nohup java -jar auth-demo-0.0.1-SNAPSHOT.jar --server.servlet.context-path=/api > /home/ubuntu/authLog.out 2>&1 &

#리소스서버 빌드 및 배포
cd $BASE_PATH"/backend"
chmod 755 gradlew
./gradlew build
cd $BASE_PATH"/backend/build/libs"
nohup java -jar hunsu-0.0.1-SNAPSHOT.jar --server.servlet.context-path=/api > /home/ubuntu/resourceLog.out 2>&1 &


#채팅서버 빌드 및 배포
cd $BASE_PATH"/chat"
chmod 755 gradlew
./gradlew build
cd $BASE_PATH"/chat/build/libs"
nohup java -jar chat-0.0.1-SNAPSHOT.jar --server.servlet.context-path=/api > /home/ubuntu/resourceLog.out 2>&1 &

#프론트 빌드 및 배포
cd $BASE_PATH"/frontend/hunsu"
npm install
npm run build


#Nginx 재시작
sudo service nginx restart
#/home/ubuntu/hunsu/s04p13c102/frontend/hunsu/index.html