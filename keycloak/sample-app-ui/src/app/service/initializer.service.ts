import {KeycloakService} from 'keycloak-angular';
import {environment as env} from '../../environments/environment';


export function initializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: env.keycloakConfig.url,
            realm: env.keycloakConfig.realm,
            clientId: env.keycloakConfig.clientId,
          },
          loadUserProfileAtStartUp: true,
          initOptions: {
            onLoad: 'login-required'
          },
          bearerExcludedUrls: []
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
