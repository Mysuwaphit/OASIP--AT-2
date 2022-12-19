import { createStore } from 'vuex';

const store = createStore({
  state() {
    return {
      msalConfig: {
        auth: {
          clientId: 'b0b24b6e-4423-4ef8-92f6-c2581842c788',
          authority: "https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d",
        //   redirectUri: "http://localhost:80/at2/",
        },
        cache: {
          cacheLocation: 'localStorage',
        },
      },
      accessToken: ''
    };
  },
  mutations :{
    setAccessToken(state, token){
      state.accessToken = token;
    }
  }
});

export default store;