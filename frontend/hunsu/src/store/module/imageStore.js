import AWS from 'aws-sdk';

const state = {
  albumBucketName: 'hunsutest',
  bucketRegion: 'ap-northeast-2',
  IdentityPoolId: 'ap-northeast-2:05f4caec-c58a-480d-9d25-8cacd2e2dea0',
  uploadImageUrls: [],
  uploadImageFiles: [],
  ootdInfoImages: [],
};
const getters = {
  getUploadImageUrls(state) {
    return state.uploadImageUrls;
  },
  getUploadImageFiles(state) {
    return state.uploadImageFiles;
  },
  getOotdInfoImages(state) {
    return state.ootdInfoImages;
  },
};

const mutations = {
  setUploadImageUrls(state) {
    state.UploadImageUrls = [];
    state.uploadImageFiles.forEach((file) => {
      state.uploadImageUrls.push(URL.createObjectURL(file));
    });
  },
  setUploadImageFiles(state, payload) {
    state.uploadImageFiles = [];
    payload.forEach((file) => {
      state.uploadImageFiles.push(file);
    });
    console.log('gd ', state.uploadImageFiles);
  },
  clearUploads(state) {
    state.uploadImageUrls = [];
    state.uploadImageFiles = [];
  },
  setOotdInfoImages(state, payload) {
    state.ootdInfoImages = payload;
  },
};

const actions = {
  async uploadImage({ state }, { key, articleIdx }) {
    // 이미지 업로드
    console.log('uploadImages', key);
    await state.uploadImageFiles.forEach((imageFile, idx) => {
      console.log('file', imageFile);
      //파일 확장자
      let fileExtList = imageFile.name.split('.');
      let fileExt = fileExtList[fileExtList.length - 1];
      console.log(fileExt);

      s3.upload(
        {
          Key: key + articleIdx + '/' + (idx + 1) + '.' + fileExt,
          Body: imageFile,
          ACL: 'public-read',
          ContentType: 'image/' + fileExt,
        },
        (err, data) => {
          if (err) {
            console.log(err);
            return alert('There was an error uploading your photo: ', err.message);
          }
          console.log('Successfully uploaded photo.');
          console.log(data);
        }
      );
    });
  },
  uploadProfile({ rootState, state }) {
    // 프로필사진 업로드
    state.uploadImageFiles.forEach((imageFile) => {
      console.log('profilefile', imageFile);
      //파일 확장자
      let fileExtList = imageFile.name.split('.');
      let fileExt = fileExtList[fileExtList.length - 1];
      console.log(fileExt);

      s3.upload(
        {
          Key: 'mypage/' + rootState.user.nickname + '/' + rootState.user.nickname,
          Body: imageFile,
          ACL: 'public-read',
          ContentType: 'image/' + fileExt,
        },
        (err, data) => {
          if (err) {
            console.log(err);
            return alert('There was an error uploading your photo: ', err.message);
          }
          console.log('Successfully uploaded photo.');
          console.log(data);
        }
      );
    });
  },
  getImageList(context, { prefix }) {
    // prefix에 위치한 이미지 리스트 가져오기
    let images = [];
    return new Promise((resolve, reject) => {
      s3.listObjectsV2({ Prefix: prefix }, (err, data) => {
        if (err) {
          reject('getImageList err', err);
        } else {
          data.Contents.map((key) => {
            s3.getSignedUrl(
              'getObject',
              {
                Bucket: this.albumBucketName,
                Key: key.Key,
              },
              (err, data) => {
                if (err) {
                  return alert('There was an error listing your photo: ', err.message);
                } else {
                  // console.log('in data', data);
                  images.push(data);
                }
              }
            );
          });
          resolve(images);
        }
      });
    });
  },
  async getProfiles(context, list) {
    // 게시글 내에 위치할 프로필사진들 가져오기
    console.log('list', list);
    await list.map((info) => {
      console.log(info);
      s3.getSignedUrl(
        'getObject',
        {
          Bucket: this.albumBucketName,
          Key: 'mypage/' + info.publisher + '/' + info.publisher,
        },
        (err, data) => {
          if (err) {
            return alert('There was an error listing your photo: ', err.message);
          } else {
            console.log('getImage', data);
            info.profileImage = data;
          }
        }
      );
    });
  },
  getProfileImage({ rootState }, { nickname, target }) {
    // 마이페이지 및 디테일에서의 프로필 이미지 가져오기
    let prefix = 'mypage/' + nickname;
    let getImages = new Promise((resolve, reject) => {
      s3.listObjectsV2({ Prefix: prefix }, (err, data) => {
        if (data.Contents.length === 0) {
          reject(null);
          return;
        }
        if (err) {
          // reject('getImageList err', err);
          console.log(err);
        } else {
          // console.log(data.Contents[1]);
          s3.getSignedUrl(
            'getObject',
            {
              Bucket: this.albumBucketName,
              Key: data.Contents[1].Key,
            },
            (err, data) => {
              if (err) {
                return alert('There was an error listing your photo: ', err.message);
              } else {
                // console.log('in profile data', data);
                resolve(data);
              }
            }
          );
        }
      });
    });
    getImages
      .then((image) => {
        if (target === 'my') {
          rootState.user.myProfileImage = image;
        } else if (target === 'target') {
          rootState.user.targetProfileImage = image;
        }
        return image;
      })
      .catch(() => {
        if (target === 'my') {
          rootState.user.myProfileImage = null;
        } else {
          rootState.user.targetProfileImage = null;
        }
      });
  },
};
AWS.config.update({
  region: state.bucketRegion,
  credentials: new AWS.CognitoIdentityCredentials({
    IdentityPoolId: state.IdentityPoolId,
  }),
});

var s3 = new AWS.S3({
  apiVersion: '2006-03-01',
  params: {
    Bucket: state.albumBucketName,
  },
});

export default {
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
