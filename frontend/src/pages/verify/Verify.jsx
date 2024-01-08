import {useEffect, useState} from "react";
import {verify, whoAmI} from "../../common/api";
import AuthenticationService from "../../service/AuthenticationService";

const Verify = ({snackbar}) => {
    const queryParameters = new URLSearchParams(window.location.search)
    const [userData, setUserData] = useState({
        username: "",
        code: queryParameters.get("code"),
    });

    const handleVerify = () => {
        verify(userData.code)
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

    useEffect(() => {
        handleVerify();
    }, []);

    return (
        <>
        </>
    );
};

export default Verify;
