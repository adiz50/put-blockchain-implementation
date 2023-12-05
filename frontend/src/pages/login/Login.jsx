import {useState} from "react";
import {login} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";
import {Button, Card, Input, Layout, Space, Typography} from "antd";
import {Link} from "react-router-dom";

const Login = ({snackbar}) => {
    const [userData, setUserData] = useState({
        username: "",
        password: "",
    });

    const handleLogin = () => {
        login(userData.username, userData.password)
            .then((data) => {
                AuthenticationService.registerSuccessfulLogin(
                    userData.username,
                    data.token
                );
            })
            .catch((error) => {
                snackbar(error.message, "error");
            });
    };

    return (
        <>
            <Layout.Content className="centered">
                <Card size="small" style={{width: 250}}>
                    <Space size={24} direction="vertical" style={{width: "100%"}}>
                        <Typography.Title level={3}>Login</Typography.Title>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Input
                                placeholder="Username"
                                value={userData.username}
                                onChange={(e) =>
                                    setUserData({...userData, username: e.target.value})
                                }
                            />
                            <Input.Password
                                placeholder="Password"
                                value={userData.password}
                                onChange={(e) =>
                                    setUserData({...userData, password: e.target.value})
                                }
                            />
                            <Link to="/forgotten">Forgot password?</Link>
                        </Space>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Button
                                type="primary"
                                onClick={handleLogin}
                                style={{width: "100%"}}
                            >
                                Login
                            </Button>
                            <Button
                                onClick={() => {
                                    window.location.href = "/register";
                                }}
                                style={{width: "100%"}}
                            >
                                Register
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </Layout.Content>
        </>
    );
};

export default Login;
