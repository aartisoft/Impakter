package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnEditClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.object.CollectionRespond;

import java.util.List;

public class CollectionOtherPeopleAdapter extends RecyclerView.Adapter {
    private static final int TYPE_NO_IMAGES = 0;
    private static final int TYPE_ONE_IMAGE = 1;
    private static final int TYPE_TWO_IMAGES = 2;
    private static final int TYPE_THREE_IMAGES = 3;
    private static final int TYPE_FOUR_IMAGES = 4;
    private static final int TYPE_FIVE_IMAGES = 5;
    private static final int TYPE_MORE_FIVE_IMAGES = 6;
    private static final int TYPE_MORE_SEVEN_IMAGES = 7;

    private Activity context;
    private List<CollectionObj> listCollections;
    private LayoutInflater inflater;
    private int wScreen;
    private OnItemClickListener onItemClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private OnShareClickListener onShareClickListener;
    private OnEditClickListener onEditClickListener;

    public CollectionOtherPeopleAdapter(Activity context, List<CollectionObj> listCollections) {
        this.context = context;
        this.listCollections = listCollections;
        inflater = LayoutInflater.from(context);
        wScreen = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getItemViewType(int position) {
        int size = listCollections.get(position).getListProduct().size();
        if (size == 0) {
            return TYPE_NO_IMAGES;
        } else if (size == 1) {
            return TYPE_ONE_IMAGE;
        } else if (size == 2) {
            return TYPE_TWO_IMAGES;
        } else if (size == 3) {
            return TYPE_THREE_IMAGES;
        } else if (size == 4) {
            return TYPE_FOUR_IMAGES;
        } else if (size == 5) {
            return TYPE_FIVE_IMAGES;
        } else if (size == 6) {
            return TYPE_MORE_FIVE_IMAGES;
        } else {
            return TYPE_MORE_SEVEN_IMAGES;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_IMAGES) {
            View view = inflater.inflate(R.layout.item_list_collection_no_collection, parent, false);
            return new NoImageViewHolder(view);
        } else if (viewType == TYPE_ONE_IMAGE) {
            View view = inflater.inflate(R.layout.item_list_collection_one_image, parent, false);
            return new OneImageViewHolder(view);
        } else if (viewType == TYPE_TWO_IMAGES) {
            View view = inflater.inflate(R.layout.item_list_collection_two_images, parent, false);
            return new TwoImageViewHolder(view);
        } else if (viewType == TYPE_THREE_IMAGES) {
            View view = inflater.inflate(R.layout.item_list_collection_three_images, parent, false);
            return new ThreeImageViewHolder(view);
        } else if (viewType == TYPE_FOUR_IMAGES) {
            View view = inflater.inflate(R.layout.item_list_collection_four_images, parent, false);
            return new FourImageViewHolder(view);
        } else if (viewType == TYPE_MORE_FIVE_IMAGES) {
            View view = inflater.inflate(R.layout.item_list_collection_five_images, parent, false);
            return new FiveImageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_list_collection_seven_images, parent, false);
            return new SevenImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CollectionObj data = listCollections.get(position);
        if (holder instanceof NoImageViewHolder) {
            ((NoImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((NoImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");
        } else if (holder instanceof OneImageViewHolder) {
            ((OneImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((OneImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");
            if (data.getListProduct().size() != 0)
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((OneImageViewHolder) holder).imageOne);
        } else if (holder instanceof TwoImageViewHolder) {
            ((TwoImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((TwoImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");
            if (data.getListProduct().size() > 1) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((TwoImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((TwoImageViewHolder) holder).imageTwo);
            }

        } else if (holder instanceof ThreeImageViewHolder) {
            ((ThreeImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((ThreeImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");

            if (data.getListProduct().size() > 2) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((ThreeImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((ThreeImageViewHolder) holder).imageTwo);
                Glide.with(context).load(data.getListProduct().get(2).getProductImage()).into(((ThreeImageViewHolder) holder).imageThree);
            }

        } else if (holder instanceof FourImageViewHolder) {
            ((FourImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((FourImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");

            if (data.getListProduct().size() > 3) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((FourImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((FourImageViewHolder) holder).imageTwo);
                Glide.with(context).load(data.getListProduct().get(2).getProductImage()).into(((FourImageViewHolder) holder).imageThree);
                Glide.with(context).load(data.getListProduct().get(3).getProductImage()).into(((FourImageViewHolder) holder).imageFour);
            }

        } else if (holder instanceof FiveImageViewHolder) {
            ((FiveImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((FiveImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");

            if (data.getListProduct().size() > 4) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((FiveImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((FiveImageViewHolder) holder).imageTwo);
                Glide.with(context).load(data.getListProduct().get(2).getProductImage()).into(((FiveImageViewHolder) holder).imageThree);
                Glide.with(context).load(data.getListProduct().get(3).getProductImage()).into(((FiveImageViewHolder) holder).imageFour);
                Glide.with(context).load(data.getListProduct().get(4).getProductImage()).into(((FiveImageViewHolder) holder).imageFive);
            }

        } else if (holder instanceof MoreFiveImageViewHolder) {
            ((MoreFiveImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((MoreFiveImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");

            if (data.getListProduct().size() > 5) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageTwo);
                Glide.with(context).load(data.getListProduct().get(2).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageThree);
                Glide.with(context).load(data.getListProduct().get(3).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageFour);
                Glide.with(context).load(data.getListProduct().get(4).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageFive);
                Glide.with(context).load(data.getListProduct().get(5).getProductImage()).into(((MoreFiveImageViewHolder) holder).imageSix);
            }
        } else if (holder instanceof SevenImageViewHolder) {
            ((SevenImageViewHolder) holder).tvCollectionName.setText(data.getCollectionName());
            ((SevenImageViewHolder) holder).tvNumberLike.setText(data.getTotalFavorite() + "");

            if (data.getListProduct().size() > 6) {
                Glide.with(context).load(data.getListProduct().get(0).getProductImage()).into(((SevenImageViewHolder) holder).imageOne);
                Glide.with(context).load(data.getListProduct().get(1).getProductImage()).into(((SevenImageViewHolder) holder).imageTwo);
                Glide.with(context).load(data.getListProduct().get(2).getProductImage()).into(((SevenImageViewHolder) holder).imageThree);
                Glide.with(context).load(data.getListProduct().get(3).getProductImage()).into(((SevenImageViewHolder) holder).imageFour);
                Glide.with(context).load(data.getListProduct().get(4).getProductImage()).into(((SevenImageViewHolder) holder).imageFive);
                Glide.with(context).load(data.getListProduct().get(5).getProductImage()).into(((SevenImageViewHolder) holder).imageSix);
                Glide.with(context).load(data.getListProduct().get(6).getProductImage()).into(((SevenImageViewHolder) holder).imageSeven);
            }
        }


    }

    @Override
    public int getItemCount() {
        return listCollections.size();
    }

    class NoImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivEdit.setVisibility(View.GONE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
        }
    }

    class OneImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public OneImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    class TwoImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public TwoImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    class ThreeImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo, imageThree;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public ThreeImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);
            imageThree = itemView.findViewById(R.id.image_three);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

        }
    }

    class FourImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo, imageThree, imageFour;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public FourImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);
            imageThree = itemView.findViewById(R.id.image_three);
            imageFour = itemView.findViewById(R.id.image_four);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

        }
    }

    class FiveImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo, imageThree, imageFour, imageFive;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public FiveImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);
            imageThree = itemView.findViewById(R.id.image_three);
            imageFour = itemView.findViewById(R.id.image_four);
            imageFive = itemView.findViewById(R.id.image_five);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    class MoreFiveImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo, imageThree, imageFour, imageFive, imageSix;
        private CardView ivAddProduct;
        private LinearLayout layoutFavorite;

        public MoreFiveImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAddProduct = itemView.findViewById(R.id.iv_add_product);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);
            imageThree = itemView.findViewById(R.id.image_three);
            imageFour = itemView.findViewById(R.id.image_four);
            imageFive = itemView.findViewById(R.id.image_five);
            imageSix = itemView.findViewById(R.id.image_six);

            ivEdit.setVisibility(View.GONE);
            ivAddProduct.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

        }
    }

    class SevenImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCollectionName, tvNumberLike;
        private ImageView ivShare, ivEdit, ivDelete;
        private ImageView imageOne, imageTwo, imageThree, imageFour, imageFive, imageSix, imageSeven;
        private LinearLayout layoutFavorite;

        public SevenImageViewHolder(final View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);

            ivShare = itemView.findViewById(R.id.iv_share);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            imageOne = itemView.findViewById(R.id.image_one);
            imageTwo = itemView.findViewById(R.id.image_two);
            imageThree = itemView.findViewById(R.id.image_three);
            imageFour = itemView.findViewById(R.id.image_four);
            imageFive = itemView.findViewById(R.id.image_five);
            imageSix = itemView.findViewById(R.id.image_six);
            imageSeven = itemView.findViewById(R.id.image_seven);

            ivEdit.setVisibility(View.INVISIBLE);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
            layoutFavorite = itemView.findViewById(R.id.layout_favorite);

            layoutFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(layoutFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener onFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }
}
