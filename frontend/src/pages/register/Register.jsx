import {useEffect, useState} from "react";
import {registerUser} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";
import {Button, Card, Input, Layout, Space, Typography, Modal} from "antd";

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
    const [emailInfo, setEmailInfo] = useState({
        open: false,
    });
    const [passwordInfo, setPasswordInfo] = useState({
        open: false,
    });

    const showPasswordInfo = () => {
        setPasswordInfo({
            open: true,
        });
    };

    const handleRegister = () => {
        registerUser(userData.username, userData.password, userData.email)
            .then((data) => {
                AuthenticationService.registerSuccessfulLogin(
                    userData.username,
                    data.token
                );
                setEmailInfo({open: true});
            })
            .catch((error) => {
                snackbar(error.message, "error");
            });
    };

    const closePswdInfo = () => {
        setPasswordInfo({
            open: false,
        });
    }

    const handleClose = () => {
        setEmailInfo({
            open: false,
        });
        window.location.href = "/login"
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
                            <div
                                style={{
                                    display: "inline-flex",
                                }}
                            >
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
                                <Button
                                    style={{ 
                                        backgroundColor: "rgba(0, 0, 0, 0.04)",
                                    }}
                                    onClick={showPasswordInfo}
                                >
                                        ?
                                </Button>
                            </div>
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
            <Modal
                open={emailInfo.open}
                title="Successful registration"
                centered
                onCancel={handleClose}
                footer={[
                    <Button onClick={handleClose}>Understand</Button>,
                 ]}
            >
                <Space size={12} direction="vertical" style={{ width: "100%" }}>
                    <Space size={2} direction="vertical" style={{ width: "100%" }}>
                        <Typography.Text>
                            Your registration has been successful. 
                            To log in, please follow the instructions sent to your email.
                        </Typography.Text>
                    </Space>
                </Space>
            </Modal>
            <Modal
                open={passwordInfo.open}
                title="Password Requirements:"
                centered
                onCancel={closePswdInfo}
                footer={[
                    <Button onClick={closePswdInfo}>OK</Button>,
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

export default Register;
