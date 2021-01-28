import axios from '@/services/axios';

/*
    회원가입처리 및 카카오 리프레시토큰 관리, api 리프레시토큰 관리 추가해줘야함
*/
// 카카오로그인 버튼 -> 카카오 페이지 로그인 완료 -> 인가코드(함수1) -> 액세스토큰(함수2);;
// 액세스토큰으로 서버인증(함수3)
// 함수 4 = 함수1 -> 함수2 -> 함수3 :: jwt
// X-AUTH-TOKEN : jwt AccessToken
const kakaoHeader = {
    'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
};

//함수 4, 카카오 로그인 -> 인가코드 -> 액세스토큰 -> 서버인증 및 유저정보 가져오기
const kakaoLogin = async (code) => {
    const accessToken = getKakaoToken(code); //액세스토큰
    const { apiAccessToken, apiRefreshToken } = kakaoLoginInAuthServer(accessToken);
    const userData = getUserInfo(apiAccessToken); //userData
    apiRefreshToken;
    return userData;
};

//함수 1, 인가코드로 카카오 액세스토큰 받아온다.
const getKakaoToken = async (code) => {
    console.log('loginWithKakao');
    try {
        const data = {
            grant_type: 'authorization_code',
            client_id: '8e53809b2827c367b5aa27ac70dfd124',
            redirect_uri: 'http://localhost:8080/auth',
            client_secret: '71TylRM4GB5dWKVAiS7Kf1rsOZZgDWGa',
            code: code,
        };
        const queryString = Object.keys(data)
            .map((k) => encodeURIComponent(k) + '=' + encodeURIComponent(data[k]))
            .join('&');
        const result = await axios.post('https://kauth.kakao.com/oauth/token', queryString, {
            headers: kakaoHeader,
        });
        console.log('카카오 토큰', queryString);
        return result;
    } catch (e) {
        return e;
    }
};

// 함수 2, 인증서버에 소셜 로그인 처리후 api 액세스토큰 받아온다.
const kakaoLoginInAuthServer = async (accessToken) => {
    const { apiAccessToken, apiRefreshToken } = axios.post({
        url: 'http://localhost:9000/v1/auth/signin/kakao',
        params: {
            socialAccessToken: accessToken,
        },
    });
    apiRefreshToken;
    return { apiAccessToken, apiRefreshToken };
};

// 함수 3, 인증서버에 api 액세스토큰으로 계정정보 받아옴
const getUserInfo = async (apiAccessToken, apiRefreshToken) => {
    let userInfo = axios.post({
        url: 'http://localhost:9000/v1/user',
        headers: { 'X-AUTH-TOKEN': apiAccessToken },
        params: {
            lang: 'ko',
        },
    });
    apiRefreshToken;
    return userInfo;
};

export { kakaoLogin };
