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
    console.log('list', list);
    await list.map((info) => {
      console.log(info);
      s3.getSignedUrl(
        'getObject',
        {
          Bucket: this.albumBucketName,
          Key: 'mypage/' + info.nickname + '/' + info.nickname,
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
  downloadImages(context, imageUrl) {
    s3.getSignedUrl(
      'getObject',
      {
        Bucket: this.albumBucketName,
        //   Key: 'https://hunsutest.s3.ap-northeast-2.amazonaws.com/1.jpg',
        Key: imageUrl,
      },
      (err, data) => {
        if (err) {
          return alert('There was an error listing your photo: ', err.message);
        } else {
          console.log(data);
        }
      }
    );
  },
  getProfileImage({ rootState }, { nickname, target }) {
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
