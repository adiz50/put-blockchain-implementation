import {Button, Card, Input, Layout, Space, Typography} from "antd";
import {useEffect, useState } from "react";
import {getPosts} from "../../common/api";
import {Post} from "D:/Studia/Blockchain/put-blockchain-implementation/frontend/src/pages/homepage/Post.jsx";

const HomePage = ({snackbar}) => {
    const [allPosts, setAllPosts] = useState({});

    const getAllPosts = () => {
        getPosts()
            .then((data) => {
                setAllPosts(data);
            })
            .catch((error) => {
                snackbar(error.message, "error");
            });
    };

    useEffect(() => {
        const getAllData = () => {
            getAllPosts();
        };
        const intervalId = setInterval(getAllData, 5000);
        return () => {
            clearInterval(intervalId);
        };
    }, []);
    
    useEffect(getAllPosts, []);

    return (
        <>
            <Layout.Content className="centered">
                {allPosts?.map((post) => (
                    <Post post={post}/>
                ))};
            </Layout.Content>
        </>
    );
};

export default HomePage;
