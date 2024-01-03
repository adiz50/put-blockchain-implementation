import {
    Card,
    Space,
    Tag,
    Typography,
    Button,
    Input,
    Modal,
    } from "antd";

import { sendComment } from "../../common/api";
import dayjs from "dayjs";
import { useState } from 'react';
import {getPosts} from "../../common/api";
import "./Post.css";
import Comment from "./Comment.jsx";
import AuthenticationService from "../../service/AuthenticationService";

const Post = ({ post, getAllPosts }) => {

    const isUserLoggedIn = AuthenticationService.isUserLoggedIn();

    const [newComment, setNewComment] = useState({
        post: post,
        text: "",
        open: false,
    });

    const handleComment = (post, text) => {
        sendComment(post, text)
            .then((data) => {
                handleClose();
            })
            .catch((error) => {
                post(error.message, "error");
            });
      };

    const handleClose = () => {
        setNewComment({
            post: post,
            text: "",
            open: false,
        });
        getAllPosts();
    };

return (
    <>
        <div
            style={{
            display: "flex",
            flexDirection: "column",
            height: "100%",
            padding: 24,
            }}
        >
            <Card
                size="small"
                title={post.title}
            >
                <Space align="start" size={32} stlye={{ width: "100%", flexShrink: 1}}>
                    <Space direction="vertical">
                        <Space align="center">
                            <Tag color="blue">{dayjs(post.date).format("YYYY-MM-DD HH:mm:ss")}</Tag>
                        </Space>
                        <Space align="center">
                            <Typography.Text>
                                <Tag style={{
                                        maxWidth: "100%", 
                                        whiteSpace: 'pre-wrap',
                                        overflowWrap: "break-word"
                                    }}>
                                        {post.body}
                                </Tag>
                            </Typography.Text>
                        </Space>
                    </Space>
                </Space>
            </Card>
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    marginLeft: "50px",
                    width: "95%",
                }}
            >
                <div
                    className="comment-container"
                >
                    {post.comments?.map((comment) => (
                        <Comment key={comment.id} comment={comment}/>
                    ))}
                </div>
                <Button
                    type="primary"
                    className="comment-add"
                    onClick={() => {
                        isUserLoggedIn ? (
                            setNewComment({
                                ...newComment,
                                open: true,
                            })                            
                        ) : (
                            window.location.href = "/login"
                        )
                    }}
                >
                    Add comment
                </Button>
            </div>
        </div>
        <Modal
            open={newComment.open}
            title="New comment"
            centered
            onCancel={handleClose}
            footer={[
            <Button onClick={handleClose}>Cancel</Button>,
            <Button
                type="primary"
                onClick={() =>
                    handleComment(newComment.post, newComment.text)
                }
                disabled={
                    !newComment.text    
                }
            >
                Send
            </Button>,
            ]}
      >
        <Space size={12} direction="vertical" style={{ width: "100%" }}>
            <Space size={2} direction="vertical" style={{ width: "100%" }}>
                <Typography.Text>Enter text below</Typography.Text>
                <Input
                    style={{ width: "100%" }}
                    placeholder="Text"
                    type="text"
                    value={newComment.text}
                    onChange={(event) =>
                        setNewComment({
                            ...newComment,
                            text: event.target.value,
                        })
                    }
                    status={newComment.text === '' ? "error" : ""}
                />
            </Space>
        </Space>
      </Modal>
    </>
    );
};

export default Post;
  