import { Routes, Route } from "react-router-dom";
import "./App.css";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import Dashboard from "./pages/dashboard/dashboard";
import AuthenticationService from "./service/AuthenticationService";
import { Layout, message } from "antd";

function App() {
  const isUserLoggedIn = AuthenticationService.isUserLoggedIn();

  const [messageApi, contextHolder] = message.useMessage();

  const snackbar = (msg, type) => {
    messageApi[type]({
      type,
      content: msg,
    });
  };

  return (
    <Layout className="fullbody">
      <Routes>
        <Route
          path="/"
          element={
            isUserLoggedIn ? (
              <Dashboard snackbar={snackbar} />
            ) : (
              <Login snackbar={snackbar} />
            )
          }
        />
        <Route path="/login" element={<Login snackbar={snackbar} />} />
        <Route path="/register" element={<Register snackbar={snackbar} />} />
      </Routes>
      {contextHolder}
    </Layout>
  );
}

export default App;
