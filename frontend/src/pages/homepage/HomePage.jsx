import {
    Layout,
    Button,
    Space,
    Input,
    Typography,
    theme,
  } from "antd";

import {useEffect, useState, useCallback } from "react";
import {getPosts} from "../../common/api";
import Post from "./Post.jsx";

const HomePage = ({snackbar}) => {
    const { token } = theme.useToken();
    const [allPosts, setAllPosts] = useState([]);
    const [filteredPosts, setFilteredPosts] = useState([]);
    const [newSearch, setNewSearch] = useState({
        text: "",
    });

    const getAllPosts = () => {
        getPosts()
            .then((data) => {
                setAllPosts(data.posts);
                filterData(data.posts)                
            })
            .catch((error) => {
                snackbar(error.message, "error");
            });
    };

    const filterData = useCallback((posts) => {
        console.log(posts);
        console.log(newSearch);
        if (newSearch.text !== '') {
            console.log('if')
            setFilteredPosts(posts.filter(post => 
                    post.title.toLowerCase().includes(newSearch.text.toLowerCase())
                ));
        } else {
            setFilteredPosts(posts);
        }
    }, [newSearch]);
    
    useEffect(() => {
        getAllPosts();        
        return () => {};
    }, []);

    useEffect(() => {
        filterData(allPosts);
    }, [newSearch.text, filterData]);

    return (
    <>
        <Layout.Header
            style={{
                background: token.colorBgContainer,
            }}
        >
            <Space
                direction="horizontal"
                align="center"
                style={{
                    height: "100%",
                    width: "100%",
                    justifyContent: "space-between",
                }}
            >
                <Typography.Title level={3}>HomePage</Typography.Title>
                <Space size={1}>
                    <Input
                        placeholder="Search..."
                        type="text"
                        value={newSearch.text}
                        onChange={(event) => {
                            setNewSearch(() => ({
                                ...newSearch,
                                text: event.target.value,
                            }));
                        }}
                    />
                    <Button
                        type="link"
                        onClick={() => {
                            window.location.href = "/";
                        }}
                    >
                        Dashboard
                    </Button>
                </Space>
            </Space>
        </Layout.Header>
        <Layout.Content
            >
            <div
            style={{
                width: "100%",
                height: "100%",
                overflow: "hidden auto",
            }}
            >
                <div>
                    {console.log(filteredPosts)}
                    {filteredPosts?.map((post) => (
                        <Post key={post.id} post={post} getAllPosts={getAllPosts} />
                    ))}
                </div>
            </div>
        </Layout.Content>
    </>
    );
};

export default HomePage;
