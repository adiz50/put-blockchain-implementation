import {useState} from "react";
import {forgotPassword} from "../../common/api";
import {Button, Card, Input, Layout, Space, Typography} from "antd";

const ForgotPassword = ({snackbar}) => {
    const [userData, setUserData] = useState({
        email: "",
    });

    const handleForgotPassword = () => {
        forgotPassword(userData.email)
            .then((data) => {
                window.location.href = "/login";
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
                        <Typography.Title level={3}>Forgotten password</Typography.Title>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Input
                                placeholder="Email"
                                value={userData.email}
                                onChange={(e) =>
                                    setUserData({...userData, email: e.target.value})
                                }
                            />
                        </Space>
                        <Space size={12} direction="vertical" style={{width: "100%"}}>
                            <Button
                                type="primary"
                                onClick={handleForgotPassword}
                                style={{width: "100%"}}
                            >
                                Send email
                            </Button>
                        </Space>
                    </Space>
                </Card>
            </Layout.Content>
        </>
    );
};

export default ForgotPassword;
