import {useEffect, useState} from "react";
import {registerUser} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";
import {Button, Card, Input, Layout, Space, Typography} from "antd";

const Register = ({snackbar}) => {
    const [userData, setUserData] = useState({
        username: "",
        password: "",
        passwordrpt: "",
        email: "",
    });
    const [hasNumber, setHasNumber] = useState(false);
    const [hasSpecChar, setHasSpecChar] = useState(false);
    const [validLength, setValidLength] = useState(false);

    const handleRegister = () => {
        registerUser(userData.username, userData.password, userData.email)
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

    useEffect(() => {
        setValidLength(userData.password.length >= 6);
        setHasNumber(/\d/.test(userData.password));
        setHasSpecChar(
            /[ `!@#$%^&*()_+\-=\]{};':"\\|,.<>?~]/.test(userData.password)
        );
    }, [userData]);

    return (
        <>
            <Layout.Content className="centered">
                <Card size="small" style={{width: 250}}>
                    <Space size={24} direction="vertical" style={{width: "100%"}}>
                        <Typography.Title level={3}>Register</Typography.Title>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Input
                                placeholder="Username"
                                value={userData.username}
                                onChange={(e) =>
                                    setUserData({...userData, username: e.target.value})
                                }
                            />
                            <Input
                                placeholder="Email"
                                value={userData.email}
                                onChange={(e) =>
                                    setUserData({...userData, email: e.target.value})
                                }
                            />
                            <Input.Password
                                placeholder="Password"
                                value={userData.password}
                                onChange={(e) =>
                                    setUserData({...userData, password: e.target.value})
                                }
                                status={
                                    userData.password &&
                                    userData.passwordrpt &&
                                    !hasSpecChar &&
                                    !validLength &&
                                    !hasNumber &&
                                    userData.password !== userData.passwordrpt
                                        ? "error"
                                        : ""
                                }
                            />
                            <Input.Password
                                placeholder="Repeat password"
                                value={userData.passwordrpt}
                                onChange={(e) =>
                                    setUserData({...userData, passwordrpt: e.target.value})
                                }
                                status={
                                    userData.password &&
                                    userData.passwordrpt &&
                                    userData.password !== userData.passwordrpt
                                        ? "error"
                                        : ""
                                }
                            />
                        </Space>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Button
                                type="primary"
                                onClick={handleRegister}
                                style={{width: "100%"}}
                                disabled={
                                    !userData.username ||
                                    !userData.password ||
                                    !userData.passwordrpt ||
                                    !hasSpecChar ||
                                    !validLength ||
                                    !hasNumber ||
                                    userData.password !== userData.passwordrpt
                                }
                            >
                                Register
                            </Button>
                            <Button
                                onClick={() => {
                                    window.location.href = "/login";
                                }}
                                style={{width: "100%"}}
                            >
                                Login
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </Layout.Content>
        </>
    );
};

export default Register;
