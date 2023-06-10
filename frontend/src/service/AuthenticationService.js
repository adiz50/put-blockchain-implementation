import { whoAmI } from "../common/api";

class AuthenticationService {
  async registerSuccessfulLogin(username, token) {
    localStorage.setItem("authenticatedUser", username);
    localStorage.setItem("token", token);
    const user = await whoAmI(token);
    localStorage.setItem("user", JSON.stringify(user));
    window.location.href = "/";
  }

  logout() {
    localStorage.removeItem("authenticatedUser");
    localStorage.removeItem("token");
    window.location.href = "/login";
  }

  isUserLoggedIn() {
    let user = localStorage.getItem("authenticatedUser");
    if (user === null) return false;
    return true;
  }

  getLoggedInUserName() {
    let user = localStorage.getItem("authenticatedUser");
    if (user === null) return "";
    return user;
  }
}

export default new AuthenticationService();
