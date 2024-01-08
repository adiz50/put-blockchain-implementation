import {useState} from "react";
import {login} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";
import {Button, Card, Input, Layout, Space, Typography, Modal} from "antd";
import {Link} from "react-router-dom";

const Login = ({snackbar}) => {
    const [userData, setUserData] = useState({
        username: "",
        password: "",
    });
    const [passwordInfo, setPasswordInfo] = useState({
        open: false,
    });

    const showPasswordInfo = () => {
        setPasswordInfo({
            open: true,
        });
    };

    const handleClose = () => {
        setPasswordInfo({
            open: false,
        });
    };

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
                            <div
                                style={{
                                    display: "inline-flex",
                                }}
                            >
                                <Input.Password
                                    // style={{ width: "auto"}}
                                    placeholder="Password"
                                    value={userData.password}
                                    onChange={(e) =>
                                        setUserData({...userData, password: e.target.value})
                                    }
                                />
                                <Button
                                    style={{ 
                                        backgroundColor: "rgba(0, 0, 0, 0.04)",
                                    }}
                                    onClick={showPasswordInfo}
                                >
                                    ?
                                </Button>
                            </div>
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
                            <Button
                                onClick={() => {
                                    window.location.href = "/homepage";
                                }}
                                style={{width: "100%"}}
                            >
                                Home page
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </Layout.Content>
            <Modal
                open={passwordInfo.open}
                title="Password Requirements:"
                centered
                onCancel={handleClose}
                footer={[
                    <Button onClick={handleClose}>OK</Button>,
                 ]}
            >
                <Space size={12} direction="vertical" style={{ width: "100%" }}>
                    <Space size={2} direction="vertical" style={{ width: "100%" }}>
                        <Typography.Text>
                            <ul>
                                <li>6 Characters length</li>
                                <li>1 Digit</li>
                                <li>1 Special Character</li>
                            </ul>
                        </Typography.Text>
                    </Space>
                </Space>
            </Modal>
        </>
    );
};

export default Login;
