import AWS from 'aws-sdk';

const state = {
  albumBucketName: 'hunsutest',
  bucketRegion: 'ap-northeast-2',
  IdentityPoolId: 'ap-northeast-2:05f4caec-c58a-480d-9d25-8cacd2e2dea0',
  uploadImageUrls: [],
  uploadImageFiles: [],
};
const getters = {
  getUploadImageUrls(state) {
    return state.uploadImageUrls;
  },
  getUploadImageFiles(state) {
    return state.uploadImageFiles;
  },
};

const mutations = {
  setUploadImageUrls(state, payload) {
    state.uploadImageUrls.push(payload);
  },
  setUploadImageFiles(state, payload) {
    payload.forEach((file) => {
      state.uploadImageFiles.push(file);
    });
    console.log('gd ', state.uploadImageFiles);
  },
  clearUploads(state) {
    state.uploadImageUrls = [];
    state.uploadImageFiles = [];
  },
};

const actions = {
  uploadImage({ state }, { key, articleIdx }) {
    console.log('uploadImages', key);
    state.uploadImageFiles.forEach((imageFile, idx) => {
      console.log('file', imageFile);
      s3.upload(
        {
          Key: key + articleIdx + '/' + (idx + 1) + '.jpeg',
          Body: imageFile,
          ACL: 'public-read',
          ContentType: 'image/jpeg',
        },
        (err, data) => {
          if (err) {
            console.log(err);
            return alert('There was an error uploading your photo: ', err.message);
          }
          console.log('Successfully uploaded photo.');
          console.log(data);
          // viewAlbum(albumName);
        }
      );
    });
  },
  getImageList(context, { prefix }) {
    return new Promise((resolve, reject) => {
      s3.listObjectsV2({ Prefix: prefix }, (err, data) => {
        if (err) {
          reject('getImageList err', err);
        } else {
          // console.log('in list 1', data.Contents);
          resolve(data.Contents);
          // return data.Contents;
        }
      });
    });
  },
  getImages(context, { keys }) {
    console.log('get', keys);
    let images = [];
    keys.map((key) => {
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
            // console.log('in getimage', data);
            images.push(data);
          }
        }
      );
    });
    return images;
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
