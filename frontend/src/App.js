import {Route, Routes} from "react-router-dom";
import "./App.css";
import HomePage from "./pages/homepage/HomePage";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import Dashboard from "./pages/dashboard/dashboard";
import AuthenticationService from "./service/AuthenticationService";
import {Layout, message} from "antd";
import ForgotPassword from "./pages/forgotPassword/ForgotPassword";
import ResetPassword from "./pages/forgotPassword/ResetPassword";
import Verify from "./pages/verify/Verify";

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
                            <Dashboard snackbar={snackbar}/>
                        ) : (
                            <Login snackbar={snackbar}/>
                        )
                    }
                />
                <Route path="/verify" element={<Verify snackbar={snackbar}/>}/>
                <Route path="/homepage" element={<HomePage snackbar={snackbar}/>}/>
                <Route path="/login" element={<Login snackbar={snackbar}/>}/>
                <Route path="/register" element={<Register snackbar={snackbar}/>}/>
                <Route path="/forgotten" element={<ForgotPassword snackbar={snackbar}/>}/>
                <Route path="/reset" element={<ResetPassword snackbar={snackbar}/>}/>
            </Routes>
            {contextHolder}
        </Layout>
    );
}

export default App;
