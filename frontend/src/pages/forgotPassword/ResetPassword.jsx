import {useState} from "react";
import {resetPassword, whoAmI} from "../../common/api";
import {Button, Card, Input, Layout, Space, Typography} from "antd";
import AuthenticationService from "../../service/AuthenticationService";

const ResetPassword = ({snackbar}) => {
    const queryParameters = new URLSearchParams(window.location.search)
    const [userData, setUserData] = useState({
        username: "",
        password: "",
        passwordrpt: "",
        code: queryParameters.get("code"),
    });

    const handleResetPassword = () => {
        resetPassword(userData.password, userData.code)
            .then((data) => {
                setUserData({...userData, username: whoAmI(data.token).username})
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
                        <Typography.Title level={3}>Reset password</Typography.Title>
                        <Input.Password
                            placeholder="Password"
                            value={userData.password}
                            onChange={(e) =>
                                setUserData({...userData, password: e.target.value})
                            }
                            status={
                                userData.password &&
                                userData.passwordrpt &&
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
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Button
                                type="primary"
                                onClick={handleResetPassword}
                                style={{width: "100%"}}
                            >
                                Reset password
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </Layout.Content>
        </>
    );
};

export default ResetPassword;
