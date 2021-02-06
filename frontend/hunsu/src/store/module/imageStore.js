import AWS from 'aws-sdk';

const state = {
  albumBucketName: 'hunsutest',
  bucketRegion: 'ap-northeast-2',
  IdentityPoolId: 'ap-northeast-2:05f4caec-c58a-480d-9d25-8cacd2e2dea0',
};
const getters = {};

const mutations = {};

const actions = {
  uploadImage(context, { key, file }) {
    console.log('uploadImages', key, file);
    return s3.upload(
      {
        Key: key,
        Body: file,
        ACL: 'public-read',
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
