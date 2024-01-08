import {
    Card,
    Space,
    Tag,
    Typography,
    } from "antd";
import dayjs from "dayjs";

const Comment = ({ comment }) => {

return (
    <div
        style={{
        display: "flex",
        flexDirection: "column",
        height: "100%",
        padding: "12px",
        }}
    >
        <Card
            size="small"
            title={"Comment by: " + comment.username}
        >
            <Space align="start" size={32} stlye={{ width: "100%", flexShrink: 1}}>
                <Space direction="vertical">
                    <Space align="center">
                        <Tag color="blue">{dayjs(comment.date).format("YYYY-MM-DD HH:mm:ss")}</Tag>
                    </Space>
                    <Space align="center">
                        <Typography.Text>
                            <Tag style={{
                                    maxWidth: "100%", 
                                    whiteSpace: 'pre-wrap',
                                    overflowWrap: "break-word"
                                }}>
                                    {comment.text}
                            </Tag>
                        </Typography.Text>
                    </Space>
                </Space>
            </Space>
        </Card>
    </div>
    );
};

export default Comment;
  